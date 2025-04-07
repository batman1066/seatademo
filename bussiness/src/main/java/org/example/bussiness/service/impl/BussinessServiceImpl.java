package org.example.bussiness.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.core.model.BranchType;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.bussiness.service.BussinessService;
import org.example.bussinessapi.dto.PurchaseDTO;
import org.example.orderapi.OrderApi;
import org.example.orderapi.OrderTccApi;
import org.example.storageapi.StorageApi;
import org.example.storageapi.StorageTccApi;
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
    @DubboReference
    private StorageApi storageApi;
    @DubboReference
    private StorageTccApi storageTccApi;


    @DubboReference
    private OrderApi orderApi;

    @DubboReference
    private OrderTccApi orderTccApi;


    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "seata-xa")
    public String purchase(PurchaseDTO dto) {
        log.info("purchase begin ... xid: " + RootContext.getXID());
        storageApi.deduct(dto.getCommodityCode(), dto.getOrderCount());
        orderApi.create(dto.getUserId(), dto.getCommodityCode(), dto.getOrderCount());
        return "正在处理了";
    }

    @GlobalTransactional(timeoutMills = 300000, name = "seata-tcc")
    @Override
    public String tccPurchase(PurchaseDTO dto) {
        RootContext.bindBranchType(BranchType.TCC);
        log.info("tccPurchase begin ... xid: " + RootContext.getXID());
        String businessId = UuidUtils.generateUuid();
        //第一个TCC 事务参与者
        storageTccApi.prepare(businessId, dto.getCommodityCode(), dto.getOrderCount());
        //第二个TCC 事务参与者
        orderTccApi.prepare(businessId, dto.getUserId(), dto.getCommodityCode(), dto.getOrderCount());
        return "正在处理了";
    }
}
