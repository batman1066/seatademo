package org.example.common;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GlobalInit {
    @PostConstruct
    public void init() {

    }
}
