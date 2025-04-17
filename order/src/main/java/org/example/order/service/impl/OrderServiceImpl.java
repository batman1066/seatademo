package org.example.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.example.api.accountapi.AccountApi;
import org.example.order.service.OrderService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.Objects;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @DubboReference
    private AccountApi accountApi;

    @Override
    @Transactional
    public void create(String userId, String commodityCode, int orderCount) {
        log.info("Order Service Begin ... xid: " + RootContext.getXID());

        // 计算订单金额
        int orderMoney = calculate(commodityCode, orderCount);

        // 从账户余额扣款
        accountApi.debit(userId, orderMoney);

        log.info(
                "Order Service SQL: insert into order_tbl (user_id, commodity_code, count, money) values ({}, {}, {}, {})",
                userId, commodityCode, orderCount, orderMoney);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
                    "insert into order_tbl (user_id, commodity_code, count, money) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setObject(1, userId);
            pst.setObject(2, commodityCode);
            pst.setObject(3, orderCount);
            pst.setObject(4, orderMoney);
            return pst;
        }, keyHolder);
        if (orderMoney > 10000) {
            throw new RuntimeException("交易金额过大");
        }

        log.info("Order Service End ... Created " + Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    private int calculate(String commodityId, int orderCount) {
        return 200 * orderCount;
    }

}