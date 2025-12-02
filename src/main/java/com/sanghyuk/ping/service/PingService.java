package com.sanghyuk.ping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PingService {

    private static final Logger log = LoggerFactory.getLogger(PingService.class);
    private final RestClient restClient;

    public PingService(RestClient.Builder builder) {
        // [중요] Render 서버가 깨어나는 시간(Cold Start)을 고려해 45초로 넉넉하게 설정
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(45))
                .withReadTimeout(Duration.ofSeconds(45));

        this.restClient = builder
                .requestFactory(ClientHttpRequestFactories.get(settings))
                .build();
    }

    public void sendPing(String name, String url) {
        try {
            var response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity(); // 바디 없이 상태 코드만 확인

            log.info("[✅ SUCCESS] {} | URL: {} | Status: {}", name, url, response.getStatusCode());

        } catch (Exception e) {
            log.error("[❌ FAILURE] {} | URL: {} | Error: {}", name, url, e.getMessage());
        }
    }
}