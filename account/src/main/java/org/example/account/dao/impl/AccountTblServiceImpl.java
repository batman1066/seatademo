package org.example.account.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.account.dao.AccountTblService;
import org.example.account.domain.AccountTblDO;
import org.example.account.mapper.AccountTblMapper;
import org.springframework.stereotype.Service;

/**
 * @author yangming
 * @description 针对表【account_tbl】的数据库操作Service实现
 * @createDate 2025-04-07 15:49:02
 */
@Service
public class AccountTblServiceImpl extends ServiceImpl<AccountTblMapper, AccountTblDO>
        implements AccountTblService {

}




