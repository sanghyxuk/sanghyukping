package com.sanghyuk.ping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan // 설정 파일(@ConfigurationProperties) 스캔 활성화
@SpringBootApplication
public class SanghyukpingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SanghyukpingApplication.class, args);
    }
}