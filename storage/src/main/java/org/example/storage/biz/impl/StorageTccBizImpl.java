package org.example.storage.biz.impl;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.*;
import org.example.storage.biz.StorageTccBiz;
import org.example.storage.service.StorageTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@LocalTCC
public class StorageTccBizImpl implements StorageTccBiz {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    StorageTccService storageTccService;

    @Override
    @TwoPhaseBusinessAction(name = "AccountTcc", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
    public Boolean prepare(@BusinessActionContextParameter(paramName = "businessId") String businessId,
                           String commodityCode, Integer orderCount) {
        BusinessActionContext businessActionContext = BusinessActionContextUtil.getContext();
        log.info("AccountTccServiceImpl 收到prepare, businessActionContext:" + JSON.toJSONString(businessActionContext));
        String xid = RootContext.getXID();
        log.info("AccountTccServiceImpl 收到prepare, xid:" + xid + ", businessId:" + businessId);
        storageTccService.prepare(businessId, commodityCode, orderCount);
        log.info("StorageTccServiceImpl 完成执行prepare, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到commit, xid:" + xid + ", businessId:" + businessId);
        storageTccService.commit(actionContext);
        log.info("StorageTccServiceImpl 完成执行commit, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到rollback, xid:" + xid + ", businessId:" + businessId);
        storageTccService.rollback(actionContext);
        log.info("StorageTccServiceImpl 完成执行rollback, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

}