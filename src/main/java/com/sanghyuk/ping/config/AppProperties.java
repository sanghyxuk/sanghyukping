package com.sanghyuk.ping.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "ping")
public record AppProperties(List<Target> targets) {
    public record Target(String name, String url, String cron) {}
}