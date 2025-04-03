package org.example.storage.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.context.RootContext;
import org.example.storage.service.StorageService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {


    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void deduct(String commodityCode, int count) {
        log.info("Stock Service Begin ... xid: " + RootContext.getXID());
        log.info("Deducting inventory SQL: update stock_tbl set count = count - {} where commodity_code = {}", count,
                commodityCode);

        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
                count, commodityCode);
        log.info("Stock Service End ... ");
    }

}