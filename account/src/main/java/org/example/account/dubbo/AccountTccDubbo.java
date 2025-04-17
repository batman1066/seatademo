package org.example.account.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.account.biz.AccountTccBiz;
import org.example.api.accountapi.AccountTccApi;

import javax.annotation.Resource;

/**
 * AccountDubbo
 *
 * @author yangming
 * @date 2025/3/31 18:06
 **/
@DubboService
public class AccountTccDubbo implements AccountTccApi {
    @Resource
    private AccountTccBiz accountTccBiz;


    @Override
    public Boolean prepare(String businessId,
                           String userId, Integer money) {
        return accountTccBiz.prepare(businessId, userId, money);
    }
}
