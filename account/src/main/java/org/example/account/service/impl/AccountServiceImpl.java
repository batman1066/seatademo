package org.example.account.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.context.RootContext;
import org.example.account.service.AccountService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void debit(String userId, int money) {
        log.info("Account Service ... xid: " + RootContext.getXID());
        log.info("Deducting balance SQL: update account_tbl set money = money - {} where user_id = {}", money,
                userId);

        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", money, userId);
        log.info("Account Service End ... ");
    }

}