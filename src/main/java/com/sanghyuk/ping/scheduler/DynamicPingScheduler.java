package com.sanghyuk.ping.scheduler;

import com.sanghyuk.ping.config.AppProperties;
import com.sanghyuk.ping.service.PingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@EnableScheduling
public class DynamicPingScheduler {

    private static final Logger log = LoggerFactory.getLogger(DynamicPingScheduler.class);

    private final AppProperties appProperties;
    private final TaskScheduler taskScheduler;
    private final PingService pingService;

    public DynamicPingScheduler(AppProperties appProperties, TaskScheduler taskScheduler, PingService pingService) {
        this.appProperties = appProperties;
        this.taskScheduler = taskScheduler; // Spring Boot가 자동 설정한 기본 스케줄러 주입
        this.pingService = pingService;
    }

    @PostConstruct
    public void scheduleTasks() {
        log.info("========== 스케줄러 등록 시작 ==========");

        if (appProperties.targets() == null || appProperties.targets().isEmpty()) {
            log.warn("등록된 타겟이 없습니다. application.yml을 확인해주세요.");
            return;
        }

        for (AppProperties.Target target : appProperties.targets()) {
            Runnable task = () -> pingService.sendPing(target.name(), target.url());
            CronTrigger trigger = new CronTrigger(target.cron(), TimeZone.getDefault());

            taskScheduler.schedule(task, trigger);

            log.info("🎯 등록됨: [{}] 주기: [{}] URL: [{}]", target.name(), target.cron(), target.url());
        }

        log.info("========== 스케줄러 등록 완료 ==========");
    }
}