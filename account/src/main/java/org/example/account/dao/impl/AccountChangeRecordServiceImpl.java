package org.example.account.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.account.dao.AccountChangeRecordService;
import org.example.account.domain.AccountChangeRecordDO;
import org.example.account.mapper.AccountChangeRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author yangming
 * @description 针对表【account_change_record(任何变动都记录)】的数据库操作Service实现
 * @createDate 2025-04-07 15:49:02
 */
@Service
public class AccountChangeRecordServiceImpl extends ServiceImpl<AccountChangeRecordMapper, AccountChangeRecordDO>
        implements AccountChangeRecordService {

}




