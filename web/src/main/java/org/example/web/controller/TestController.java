package org.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.api.bussinessapi.BussinessApi;
import org.example.api.bussinessapi.dto.PurchaseDTO;
import org.example.common.BusinessException;
import org.example.common.constant.BusinessExceptionEnum;
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

    @GetMapping("/test")
    public String test(@RequestParam("hehe") String hehe) {
        log.info("rest api hehe:{}", hehe);
        mockError(hehe);
        return bussinessApi.test(hehe);
    }

    private void mockError(String hehe) {
        if ("wc".equals(hehe)) {
            int b = 1 / 0;
        } else if ("wbi".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage(), true);
        } else if ("wbo".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage());
        }
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
