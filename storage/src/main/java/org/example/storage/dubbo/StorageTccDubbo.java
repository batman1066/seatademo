package org.example.storage.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.storage.biz.StorageTccBiz;
import org.example.storageapi.StorageTccApi;

import javax.annotation.Resource;

/**
 * StorageDubbo
 *
 * @author yangming
 * @date 2025/4/3 16:52
 **/
@DubboService
public class StorageTccDubbo implements StorageTccApi {
    @Resource
    private StorageTccBiz storageTccBiz;


    @Override

    public Boolean prepare(String businessId,
                           String commodityCode, Integer orderCount) {
        return storageTccBiz.prepare(businessId, commodityCode, orderCount);
    }
}
