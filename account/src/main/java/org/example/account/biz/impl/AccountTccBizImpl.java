package org.example.account.biz.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.*;
import org.example.account.biz.AccountTccBiz;
import org.example.account.service.AccountTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@LocalTCC
public class AccountTccBizImpl implements AccountTccBiz {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    AccountTccService accountTccService;

    @Override
    @TwoPhaseBusinessAction(name = "AccountTcc", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
    public Boolean prepare(@BusinessActionContextParameter(paramName = "businessId") String businessId,
                           String userId, Integer money) {
        BusinessActionContext businessActionContext = BusinessActionContextUtil.getContext();
        log.info("AccountTccServiceImpl 收到prepare, businessActionContext:" + JSON.toJSONString(businessActionContext));
        String xid = RootContext.getXID();
        log.info("AccountTccServiceImpl 收到prepare, xid:" + xid + ", businessId:" + businessId);
        accountTccService.prepare(businessId, userId, money);
        log.info("AccountTccServiceImpl 完成执行prepare, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到commit, xid:" + xid + ", businessId:" + businessId);
        accountTccService.commit(actionContext);
        log.info("StorageTccServiceImpl 完成执行commit, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到rollback, xid:" + xid + ", businessId:" + businessId);
        accountTccService.rollback(actionContext);
        log.info("StorageTccServiceImpl 完成执行rollback, xid:" + xid + ", businessId:" + businessId);
        return true;
    }
}