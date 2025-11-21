from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

from selenium.webdriver.common.by import By
import time
from selenium.webdriver.devtools import devtools

# -------------- 配置 Selenium + CDP -----------------

options = Options()
options.add_argument("--disable-blink-features=AutomationControlled")
options.add_argument("--start-maximized")

driver = webdriver.Chrome(
    service=Service(ChromeDriverManager().install()),
    options=options
)

dev = driver.devtools
dev.create_session()

# 开启网络拦截
from selenium.webdriver.devtools.v114 import network
dev.send(network.enable())

# 保存目标 API 的返回内容
target_response = {"body": None}

TARGET_KEYWORD = "/api/your-target"   # ★ 换成你要监听的API片段


# ----------------  监听响应   -----------------

def on_response(event):
    url = event.response.url

    if TARGET_KEYWORD in url:
        print(f"[捕获到目标 API] {url}")

        # 获取 body
        body = dev.send(
            network.get_response_body(request_id=event.request_id)
        )["body"]

        print(f"[API返回数据] {body}")

        target_response["body"] = body


dev.add_listener(network.ResponseReceived, on_response)


# ----------------- 打开你的SSO首页 -----------------

driver.get("https://your-main-page.com")   # ★ 改成你的首页


# -------- 等待目标 API 完成 ----------
print("等待 API 响应中...")

while target_response["body"] is None:
    time.sleep(0.5)

print("最终捕获的 API 返回：")
print(target_response["body"])


# -------- 关闭浏览器 ----------
driver.quit()
