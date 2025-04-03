package org.example.bussiness;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class BussinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussinessApplication.class, args);
    }

}
