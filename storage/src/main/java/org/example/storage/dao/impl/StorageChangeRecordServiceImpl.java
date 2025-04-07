package org.example.storage.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.storage.dao.StorageChangeRecordService;
import org.example.storage.domain.StorageChangeRecordDO;
import org.example.storage.mapper.StorageChangeRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author yangming
 * @description 针对表【storage_change_record(任何变动都记录)】的数据库操作Service实现
 * @createDate 2025-04-07 15:48:21
 */
@Service
public class StorageChangeRecordServiceImpl extends ServiceImpl<StorageChangeRecordMapper, StorageChangeRecordDO>
        implements StorageChangeRecordService {

}




