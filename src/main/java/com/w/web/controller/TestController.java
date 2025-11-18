package com.w.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
public class TestController {

    private final WebClient webClient;

    public TestController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/test")
    public void test() {
        webClient.get()
                .uri("https://example.com/")
                .retrieve()
                .toBodilessEntity()
                .block();

        String result = webClient.post()
                .uri("https://example.com/api/login")
                .header("User-Agent", "Mozilla/5.0")
                .header("x-api-key", "123456")
                .bodyValue(Map.of(
                        "username", "will",
                        "password", "123456"
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();


    }
}
