package org.example.order.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.service.OrderService;
import org.example.api.orderapi.OrderApi;

import javax.annotation.Resource;

/**
 * OrderDubbo
 *
 * @author yangming
 * @date 2025/4/3 17:00
 **/
@DubboService
@Slf4j
public class OrderDubbo implements OrderApi {
    @Resource
    private OrderService orderService;

    @Override
    public String create(String userId, String commodityCode, Integer orderCount) {
        orderService.create(userId, commodityCode, orderCount);
        return "SUCCESS";
    }
}
