package org.example.storage.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.storage.service.StorageService;
import org.example.api.storageapi.StorageApi;

import javax.annotation.Resource;

/**
 * StorageDubbo
 *
 * @author yangming
 * @date 2025/4/3 16:52
 **/
@DubboService
public class StorageDubbo implements StorageApi {
    @Resource
    private StorageService storageService;

    @Override
    public String deduct(String commodityCode, Integer count) {
        storageService.deduct(commodityCode, count);
        return "SUCCESS";
    }
}
