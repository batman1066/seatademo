package org.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.accountapi.AccountApi;
import org.example.bussinessapi.BussinessApi;
import org.example.bussinessapi.dto.PurchaseDTO;
import org.example.mvc.wrapper.RestWrapper;
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
@RestWrapper
public class TestController {
    @DubboReference
    BussinessApi bussinessApi;
    @DubboReference
    AccountApi accountApi;

    @GetMapping("/test")
    public String test(@RequestParam("hehe") String hehe) {
        log.info("rest api hehe:{}", hehe);
        return accountApi.test(hehe);
    }

    @GetMapping("/purchase")
    public String purchase(@RequestParam(value = "userId", defaultValue = "1") String userId,
                           @RequestParam(value = "commodityCode", defaultValue = "001") String commodityCode,
                           @RequestParam(value = "orderCount", defaultValue = "1") Integer orderCount,
                           @RequestParam(value = "transactionMode", defaultValue = "AT") String transactionMode) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setUserId(userId);
        purchaseDTO.setCommodityCode(commodityCode);
        purchaseDTO.setOrderCount(orderCount);
        purchaseDTO.setTransactionMode(transactionMode);
        log.info("rest api purchaseDTO:{}", purchaseDTO);
        return bussinessApi.purchase(purchaseDTO);
    }
}
