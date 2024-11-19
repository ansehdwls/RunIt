package com.ssafy.runit;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.TimeZone;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
@EnableJpaAuditing
@EnableCaching
public class RunItApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunItApplication.class, args);

    }
    @PostConstruct
    public void init() {
        // timezone 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
