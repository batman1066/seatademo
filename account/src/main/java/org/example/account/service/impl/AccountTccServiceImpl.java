package org.example.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.LocalTCC;
import org.example.account.dao.AccountChangeRecordService;
import org.example.account.dao.AccountTblService;
import org.example.account.domain.AccountChangeRecordDO;
import org.example.account.domain.AccountTblDO;
import org.example.account.service.AccountTccService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@LocalTCC
public class AccountTccServiceImpl implements AccountTccService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    AccountChangeRecordService accountChangeRecordService;
    @Resource
    AccountTblService accountTblService;

    /*  changeRecord表：
        tcc准备时businessId看修改记录是否存在，存在就是重复执行(幂等)；提交时记录一下即可；取消时执行并记录，
        saga准备时businessId看修改记录是否存在，存在就是重复执行(幂等)或提前取消；取消时看准备记录是否存在，存在就执行并记录，不存在就加一条记录(空回滚和防悬挂)
     */
    @Override
    @Transactional
    public void prepare(String businessId,
                        String userId, Integer money) {
        //(应对幂等）先用businessId查询change_record，如果存在就抛出异常
        LambdaQueryWrapper<AccountChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getBusinessId, businessId);
        long seataCount = accountChangeRecordService.count(changeRecordDOQueryWrapper);
        if (seataCount > 0) {
            throw new RuntimeException("提前取消(saga)或重复执行");
        }
        //修改数据
        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", money, userId);
        LambdaQueryWrapper<AccountTblDO> accountDOQueryWrapper = new LambdaQueryWrapper<>();
        accountDOQueryWrapper.eq(AccountTblDO::getUserId, userId);
        AccountTblDO accountTblDO = accountTblService.getOne(accountDOQueryWrapper);
        if (accountTblDO.getMoney() < 0) {
            throw new RuntimeException("余额不足");
        }
        //记录修改记录
        Integer newMoney = accountTblDO.getMoney();
        Integer oldMoney = accountTblDO.getMoney() + money;
        AccountChangeRecordDO accountChangeRecord = new AccountChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setAccountId(accountTblDO.getId());
        accountChangeRecord.setSrcMoney(oldMoney);
        accountChangeRecord.setTargetMoney(newMoney);
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(1);
        accountChangeRecordService.save(accountChangeRecord);
    }

    @Override
    @Transactional
    public void commit(BusinessActionContext actionContext) {
        String businessId = (String) actionContext.getActionContext("businessId");
        //查询准备记录
        LambdaQueryWrapper<AccountChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getSeataStatus, 1);
        AccountChangeRecordDO accountChangeRecordPrepareDO = accountChangeRecordService.getOne(changeRecordDOQueryWrapper);
        //记录修改记录
        AccountChangeRecordDO accountChangeRecord = new AccountChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setAccountId(accountChangeRecordPrepareDO.getAccountId());
        accountChangeRecord.setSrcMoney(accountChangeRecordPrepareDO.getTargetMoney());
        accountChangeRecord.setTargetMoney(accountChangeRecordPrepareDO.getTargetMoney());
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(2);
        accountChangeRecordService.save(accountChangeRecord);
    }

    @Override
    @Transactional
    public void rollback(BusinessActionContext actionContext) {
        String businessId = (String) actionContext.getActionContext("businessId");
        //查询准备记录
        LambdaQueryWrapper<AccountChangeRecordDO> changeRecordDOQueryWrapper = new LambdaQueryWrapper<>();
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getBusinessId, businessId);
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getSeataType, 1);
        changeRecordDOQueryWrapper.eq(AccountChangeRecordDO::getSeataStatus, 1);
        AccountChangeRecordDO accountChangeRecordPrepareDO = accountChangeRecordService.getOne(changeRecordDOQueryWrapper);
        Integer orderMoney = accountChangeRecordPrepareDO.getTargetMoney() - accountChangeRecordPrepareDO.getSrcMoney();
        //恢复数据
        jdbcTemplate.update("update account_tbl set money = money + ? where id = ?",
                orderMoney, accountChangeRecordPrepareDO.getAccountId());
        //查询账户
        LambdaQueryWrapper<AccountTblDO> accountDOQueryWrapper = new LambdaQueryWrapper<>();
        accountDOQueryWrapper.eq(AccountTblDO::getId, accountChangeRecordPrepareDO.getAccountId());
        AccountTblDO accountTblDO = accountTblService.getOne(accountDOQueryWrapper);
        //记录修改记录
        Integer newMoney = accountTblDO.getMoney();
        Integer oldMoney = newMoney - orderMoney;
        AccountChangeRecordDO accountChangeRecord = new AccountChangeRecordDO();
        accountChangeRecord.setBusinessId(businessId);
        accountChangeRecord.setAccountId(accountChangeRecordPrepareDO.getAccountId());
        accountChangeRecord.setSrcMoney(oldMoney);
        accountChangeRecord.setTargetMoney(newMoney);
        accountChangeRecord.setSeataType(1);
        accountChangeRecord.setSeataStatus(3);
        accountChangeRecordService.save(accountChangeRecord);
    }
}