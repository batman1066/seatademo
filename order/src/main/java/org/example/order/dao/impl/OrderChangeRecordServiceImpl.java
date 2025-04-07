package org.example.order.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.order.dao.OrderChangeRecordService;
import org.example.order.domain.OrderChangeRecordDO;
import org.example.order.mapper.OrderChangeRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author yangming
 * @description 针对表【order_change_record(任何变动都记录)】的数据库操作Service实现
 * @createDate 2025-04-07 15:47:10
 */
@Service
public class OrderChangeRecordServiceImpl extends ServiceImpl<OrderChangeRecordMapper, OrderChangeRecordDO>
        implements OrderChangeRecordService {

}




