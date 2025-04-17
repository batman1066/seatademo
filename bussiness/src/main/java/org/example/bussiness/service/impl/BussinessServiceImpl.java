package org.example.bussiness.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.core.model.BranchType;
import org.apache.seata.saga.engine.StateMachineEngine;
import org.apache.seata.saga.statelang.domain.StateMachineInstance;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.api.accountapi.BalanceSagaApi;
import org.example.api.bussinessapi.dto.PurchaseDTO;
import org.example.api.orderapi.OrderApi;
import org.example.api.orderapi.OrderTccApi;
import org.example.api.storageapi.InventorySagaApi;
import org.example.api.storageapi.StorageApi;
import org.example.api.storageapi.StorageTccApi;
import org.example.bussiness.sagacallback.PurchaseCallback;
import org.example.bussiness.service.BussinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    @DubboReference(id = "inventorySagaApi")
    private InventorySagaApi inventorySagaApi;


    @DubboReference
    private OrderApi orderApi;
    @DubboReference
    private OrderTccApi orderTccApi;

    @DubboReference(id = "balanceSagaApi")
    private BalanceSagaApi balanceSagaApi;


    @Resource
    StateMachineEngine stateMachineEngine;


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
        //整个事务的全局变量难道要用 redis来实现？用bussiness当做key?
        //第一个TCC 事务参与者
        storageTccApi.prepare(businessId, dto.getCommodityCode(), dto.getOrderCount());
        //第二个TCC 事务参与者
        orderTccApi.prepare(businessId, dto.getUserId(), dto.getCommodityCode(), dto.getOrderCount());
        return "正在处理了";
    }

    @Override
    public String sagaPurchase(PurchaseDTO dto) {
        Map<String, Object> startParams = new HashMap<>(3);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("commodityCode", dto.getCommodityCode());
        startParams.put("count", dto.getOrderCount());
        startParams.put("amount", new BigDecimal(dto.getOrderCount() * 200));
        Map<String, Object> lastParams = new HashMap<>(3);
        lastParams.put("throwException", "true");
        startParams.put("params", lastParams);

        //整个事务的全局变量难道要用 redis来实现？用bussiness当做key?
        //async test
        String stateMachineName = "reduceInventoryAndBalance";
        PurchaseCallback purchaseCallback = new PurchaseCallback();
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKeyAsync(stateMachineName, null,
                businessKey, startParams, purchaseCallback);
        log.info("saga transaction onStart XID:{},状态机状态：{}",
                inst.getId(), inst.getStatus().getStatusString());
        return "正在处理了";
    }
}
