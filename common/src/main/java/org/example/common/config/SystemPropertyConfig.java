package org.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SystemPropertyConfig {
    @Value("${spring.application.name}")
    private String appName;

    @PostConstruct
    public void init() {
        System.setProperty("spring.application.name", appName);
    }
}