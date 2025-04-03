package org.example.account.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.account.service.AccountService;
import org.example.accountapi.AccountApi;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * AccountDubbo
 *
 * @author yangming
 * @date 2025/3/31 18:06
 **/
@DubboService
public class AccountDubbo implements AccountApi {
    @Value("${test.p}")
    String testp;
    @Resource
    private AccountService accountService;

    @Override
    public String test(String hehe) {
        return "AccountDubbo:" + testp + ":" + hehe;
    }

    @Override
    public String debit(String userId, Integer money) {
        accountService.debit(userId, money);
        return "SUCCESS";
    }


}
