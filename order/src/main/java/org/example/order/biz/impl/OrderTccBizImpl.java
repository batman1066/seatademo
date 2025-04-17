package org.example.order.biz.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.*;
import org.example.api.accountapi.AccountTccApi;
import org.example.order.biz.OrderTccBiz;
import org.example.order.service.OrderTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@LocalTCC
public class OrderTccBizImpl implements OrderTccBiz {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @DubboReference
    private AccountTccApi accountTccApi;
    @Resource
    OrderTccService orderTccService;

    @Override
    @TwoPhaseBusinessAction(name = "OrderTcc", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
    public Boolean prepare(@BusinessActionContextParameter(paramName = "businessId") String businessId,
                           String userId,
                           String commodityCode, Integer orderCount) {
        BusinessActionContext businessActionContext = BusinessActionContextUtil.getContext();
        log.info("OrderTccServiceImpl 收到prepare, businessActionContext:" + JSON.toJSONString(businessActionContext));
        String xid = RootContext.getXID();
        log.info("OrderTccServiceImpl 收到prepare, xid:" + xid + ", businessId:" + businessId);
        orderTccService.prepare(businessId, userId, commodityCode, orderCount);
        log.info("OrderTccServiceImpl 完成执行prepare, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到commit, xid:" + xid + ", businessId:" + businessId);
        orderTccService.commit(actionContext);
        log.info("StorageTccServiceImpl 完成执行commit, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    public Boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到rollback, xid:" + xid + ", businessId:" + businessId);
        orderTccService.rollback(actionContext);
        log.info("StorageTccServiceImpl 完成执行rollback, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    private int calculate(String commodityId, int orderCount) {
        return 200 * orderCount;
    }
}