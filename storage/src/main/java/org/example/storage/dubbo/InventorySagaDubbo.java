package org.example.storage.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.api.storageapi.InventorySagaApi;

@DubboService
@Slf4j
public class InventorySagaDubbo implements InventorySagaApi {


    @Override
    public boolean reduce(String businessKey, String commodityCode, int count) {
        log.info("reduce inventory succeed, count: " + count + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey) {
        log.info("compensate reduce inventory succeed, businessKey:" + businessKey);
        return true;
    }
}