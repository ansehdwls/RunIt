package com.ssafy.runit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
@EnableJpaAuditing
@EnableCaching
public class RunItApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunItApplication.class, args);
    }

}
