package org.example.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextUtil;
import org.apache.seata.rm.tcc.api.LocalTCC;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.example.api.accountapi.AccountTccApi;
import org.example.order.dao.OrderChangeRecordService;
import org.example.order.dao.OrderTblService;
import org.example.order.domain.OrderChangeRecordDO;
import org.example.order.domain.OrderTblDO;
import org.example.order.service.OrderTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@LocalTCC
public class OrderTccServiceImpl implements OrderTccService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @DubboReference
    private AccountTccApi accountTccApi;
    @Resource
    OrderChangeRecordService orderChangeRecordService;
    @Resource
    OrderTblService orderTblService;

    /*  changeRecord表：
            tcc准备时businessId看修改记录是否存在，存在就是重复执行(幂等)；提交时记录一下即可；取消时执行并记录，
            saga准备时businessId看修改记录是否存在，存在就是重复执行(幂等)或提前取消；取消时看准备记录是否存在，存在就执行并记录，不存在就加一条记录(空回滚和防悬挂)
         */

    @Override
    @ShardingSphereTransactionType(TransactionType.XA)
    @Transactional
    public Boolean prepare(String businessId,
                           String userId,
                           String commodityCode, Integer orderCount) {
        BusinessActionContext businessActionContext = BusinessActionContextUtil.getContext();
        log.info("OrderTccServiceImpl 收到prepare, businessActionContext:" + JSON.toJSONString(businessActionContext));
        String xid = RootContext.getXID();
        log.info("OrderTccServiceImpl 收到prepare, xid:" + xid + ", businessId:" + businessId);
        //(应对幂等）先用businessId查询change_record，如果存在就抛出异常
        LambdaQueryWrapper<OrderChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getBusinessId, businessId);
        long seataCount = orderChangeRecordService.count(changeRecordDOQueryWrapper);
        if (seataCount > 0) {
            throw new RuntimeException("提前取消(saga)或重复执行");
        }
        // 计算订单金额
        int orderMoney = calculate(commodityCode, orderCount);
        // 从账户余额扣款
        accountTccApi.prepare(businessId, userId, orderMoney);

        //查询订单
        OrderTblDO orderTblDO = new OrderTblDO();
        orderTblDO.setCommodityCode(commodityCode);
        orderTblDO.setCount(orderCount);
        orderTblDO.setUserId(userId);
        orderTblDO.setMoney(orderMoney);
        orderTblService.save(orderTblDO);
        //记录修改记录
        OrderChangeRecordDO orderChangeRecord = new OrderChangeRecordDO();
        orderChangeRecord.setBusinessId(businessId);
        orderChangeRecord.setOrderId(orderTblDO.getId());
        orderChangeRecord.setSrcStatus(0);
        orderChangeRecord.setTargetStatus(0);
        orderChangeRecord.setSeataType(1);
        orderChangeRecord.setSeataStatus(1);
        orderChangeRecordService.save(orderChangeRecord);
        return true;
    }

    @Override
    @ShardingSphereTransactionType(TransactionType.XA)
    @Transactional
    public Boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到commit, xid:" + xid + ", businessId:" + businessId);
        //查询准备记录
        LambdaQueryWrapper<OrderChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getSeataStatus, 1);
        OrderChangeRecordDO orderChangeRecordPrepareDO = orderChangeRecordService.getOne(changeRecordDOQueryWrapper);
        //记录修改记录
        OrderChangeRecordDO orderChangeRecord = new OrderChangeRecordDO();
        orderChangeRecord.setBusinessId(businessId);
        orderChangeRecord.setOrderId(orderChangeRecordPrepareDO.getOrderId());
        orderChangeRecord.setSrcStatus(orderChangeRecordPrepareDO.getTargetStatus());
        orderChangeRecord.setTargetStatus(orderChangeRecordPrepareDO.getTargetStatus());
        orderChangeRecord.setSeataType(1);
        orderChangeRecord.setSeataStatus(2);
        orderChangeRecordService.save(orderChangeRecord);
        log.info("StorageTccServiceImpl 完成执行commit, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    @Override
    @ShardingSphereTransactionType(TransactionType.XA)
    @Transactional
    public Boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        String businessId = (String) actionContext.getActionContext("businessId");
        log.info("StorageTccServiceImpl 收到rollback, xid:" + xid + ", businessId:" + businessId);
        //查询准备记录
        LambdaQueryWrapper<OrderChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(OrderChangeRecordDO::getSeataStatus, 1);
        OrderChangeRecordDO orderChangeRecordPrepareDO = orderChangeRecordService.getOne(changeRecordDOQueryWrapper);
        //恢复数据
        orderTblService.removeById(orderChangeRecordPrepareDO.getOrderId());
        //记录修改记录
        OrderChangeRecordDO orderChangeRecord = new OrderChangeRecordDO();
        orderChangeRecord.setBusinessId(businessId);
        orderChangeRecord.setOrderId(orderChangeRecordPrepareDO.getOrderId());
        orderChangeRecord.setSrcStatus(0);
        orderChangeRecord.setTargetStatus(0);
        orderChangeRecord.setSeataType(1);
        orderChangeRecord.setSeataStatus(3);
        orderChangeRecordService.save(orderChangeRecord);
        log.info("StorageTccServiceImpl 完成执行rollback, xid:" + xid + ", businessId:" + businessId);
        return true;
    }

    private int calculate(String commodityId, int orderCount) {
        return 200 * orderCount;
    }
}