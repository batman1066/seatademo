package org.example.account;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages =
        {"org.example.account",
                "org.example.common",
                "org.example.dubbo"})
@EnableDubbo
@MapperScan("org.example.*.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
