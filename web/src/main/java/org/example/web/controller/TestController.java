package org.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.accountapi.AccountApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author yangming
 * @date 2025/3/31 15:54
 **/
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @DubboReference
    AccountApi accountApi;
    @GetMapping("/test")
    public String test(@RequestParam("hehe") String hehe){
        log.info("rest api hehe:{}",hehe);
        return accountApi.test(hehe);
    }
}
