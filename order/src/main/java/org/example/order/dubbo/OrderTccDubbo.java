package org.example.order.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.biz.OrderTccBiz;
import org.example.orderapi.OrderTccApi;

import javax.annotation.Resource;

/**
 * OrderDubbo
 *
 * @author yangming
 * @date 2025/4/3 17:00
 **/
@DubboService
@Slf4j
public class OrderTccDubbo implements OrderTccApi {
    @Resource
    private OrderTccBiz orderTccBiz;

    @Override
    public Boolean prepare(String businessId,
                           String userId,
                           String commodityCode, Integer orderCount) {
        return orderTccBiz.prepare(businessId, userId, commodityCode, orderCount);
    }
}
