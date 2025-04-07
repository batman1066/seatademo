package org.example.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.example.storage.dao.StockTblService;
import org.example.storage.dao.StorageChangeRecordService;
import org.example.storage.domain.StockTblDO;
import org.example.storage.domain.StorageChangeRecordDO;
import org.example.storage.service.StorageTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageTccServiceImpl implements StorageTccService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    StorageChangeRecordService storageChangeRecordService;
    @Resource
    StockTblService stockTblService;


    /*  changeRecord表：
        tcc准备时businessId看修改记录是否存在，存在就是重复执行(幂等)；提交时记录一下即可；取消时执行并记录，
        saga准备时businessId看修改记录是否存在，存在就是重复执行(幂等)或提前取消；取消时看准备记录是否存在，存在就执行并记录，不存在就加一条记录(空回滚和防悬挂)
     */
    @Transactional
    @Override
    public void prepare(String businessId,
                        String commodityCode, Integer orderCount) {
        //(应对幂等）先用businessId查询change_record，如果存在就抛出异常
        LambdaQueryWrapper<StorageChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getBusinessId, businessId);
        long seataCount = storageChangeRecordService.count(changeRecordDOQueryWrapper);
        if (seataCount > 0) {
            throw new RuntimeException("提前取消(saga)或重复执行");
        }
        //修改数据
        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
                orderCount, commodityCode);
        LambdaQueryWrapper<StockTblDO> accountDOQueryWrapper = new LambdaQueryWrapper<>();
        accountDOQueryWrapper.eq(StockTblDO::getCommodityCode, commodityCode);
        StockTblDO stockTblDO = stockTblService.getOne(accountDOQueryWrapper);
        if (stockTblDO.getCount() < 0) {
            throw new RuntimeException("库存不足");
        }
        //记录修改记录
        Integer newCount = stockTblDO.getCount();
        Integer oldCount = newCount + orderCount;
        StorageChangeRecordDO accountChangeRecord = new StorageChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setStorageId(stockTblDO.getId());
        accountChangeRecord.setSrcCount(oldCount);
        accountChangeRecord.setTargetCount(newCount);
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(1);
        storageChangeRecordService.save(accountChangeRecord);
    }

    @Transactional
    @Override
    public void commit(BusinessActionContext actionContext) {
        String businessId = (String) actionContext.getActionContext("businessId");
        //查询准备记录
        LambdaQueryWrapper<StorageChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getSeataStatus, 1);
        StorageChangeRecordDO accountChangeRecordPrepareDO = storageChangeRecordService.getOne(changeRecordDOQueryWrapper);
        //记录修改记录
        StorageChangeRecordDO accountChangeRecord = new StorageChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setStorageId(accountChangeRecordPrepareDO.getStorageId());
        accountChangeRecord.setSrcCount(accountChangeRecordPrepareDO.getTargetCount());
        accountChangeRecord.setTargetCount(accountChangeRecordPrepareDO.getTargetCount());
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(2);
        storageChangeRecordService.save(accountChangeRecord);
    }

    @Transactional
    @Override
    public void rollback(BusinessActionContext actionContext) {
        String businessId = (String) actionContext.getActionContext("businessId");
        //查询准备记录
        LambdaQueryWrapper<StorageChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(StorageChangeRecordDO::getSeataStatus, 1);
        StorageChangeRecordDO accountChangeRecordPrepareDO = storageChangeRecordService.getOne(changeRecordDOQueryWrapper);
        Integer orderCount = accountChangeRecordPrepareDO.getSrcCount() - accountChangeRecordPrepareDO.getTargetCount();
        //恢复数据
        jdbcTemplate.update("update stock_tbl set count = count + ? where id = ?",
                orderCount, accountChangeRecordPrepareDO.getStorageId());
        LambdaQueryWrapper<StockTblDO> accountDOQueryWrapper = new LambdaQueryWrapper<>();
        StockTblDO stockTblDO = stockTblService.getOne(accountDOQueryWrapper);
        //记录修改记录
        Integer newCount = stockTblDO.getCount();
        Integer oldCount = newCount - orderCount;
        StorageChangeRecordDO accountChangeRecord = new StorageChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setStorageId(accountChangeRecordPrepareDO.getStorageId());
        accountChangeRecord.setSrcCount(oldCount);
        accountChangeRecord.setTargetCount(newCount);
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(3);
        storageChangeRecordService.save(accountChangeRecord);
    }
}
