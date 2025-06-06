package org.example.account.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.account.service.AccountService;
import org.example.api.accountapi.AccountApi;
import org.example.common.BusinessException;
import org.example.common.constant.BusinessExceptionEnum;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * AccountDubbo
 *
 * @author yangming
 * @date 2025/3/31 18:06
 **/
@DubboService
@Slf4j
public class AccountDubbo implements AccountApi {
    @Value("${test.p}")
    String testp;
    @Resource
    private AccountService accountService;

    @Override
    public String test(String hehe) {
        mockError(hehe);
        return "AccountDubbo:" + testp + ":" + hehe;
    }

    private void mockError(String hehe) {
        if ("ac".equals(hehe)) {
            int b = 1 / 0;
        } else if ("abi".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage(), true);
        } else if ("abo".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage());
        }
    }

    @Override
    public String debit(String userId, Integer money) {
        accountService.debit(userId, money);
        return "SUCCESS";
    }


}
