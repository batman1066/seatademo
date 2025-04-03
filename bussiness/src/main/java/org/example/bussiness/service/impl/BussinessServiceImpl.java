package org.example.bussiness.service.impl;

import com.alibaba.boot.nacos.config.autoconfigure.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.bussiness.service.BussinessService;
import org.example.bussinessapi.dto.PurchaseDTO;
import org.example.orderapi.OrderApi;
import org.example.storageapi.StorageApi;
import org.springframework.stereotype.Service;

/**
 * BussinessServiceImpl
 *
 * @author yangming
 * @date 2025/4/3 16:39
 **/
@Service
@Slf4j
public class BussinessServiceImpl implements BussinessService {

    private final NacosConfigAutoConfiguration nacosConfigAutoConfiguration;
    @DubboReference
    private StorageApi storageApi;
    @DubboReference
    private OrderApi orderApi;

    public BussinessServiceImpl(NacosConfigAutoConfiguration nacosConfigAutoConfiguration) {
        this.nacosConfigAutoConfiguration = nacosConfigAutoConfiguration;
    }


    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "seata-xa")
    public String purchase(PurchaseDTO dto) {
        log.info("purchase begin ... xid: " + RootContext.getXID());
        storageApi.deduct(dto.getCommodityCode(), dto.getOrderCount());
        orderApi.create(dto.getUserId(), dto.getCommodityCode(), dto.getOrderCount());
        return "正在处理了";
    }
}
