package com.w.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


    @Bean
    public WebClient webClient() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                .build();

        return WebClient.builder()
                .clientConnector(new JdkClientHttpConnector(httpClient))
                .build();
    }


}
