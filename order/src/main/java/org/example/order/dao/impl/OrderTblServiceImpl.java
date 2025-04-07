package org.example.order.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.order.dao.OrderTblService;
import org.example.order.domain.OrderTblDO;
import org.example.order.mapper.OrderTblMapper;
import org.springframework.stereotype.Service;

/**
 * @author yangming
 * @description 针对表【order_tbl】的数据库操作Service实现
 * @createDate 2025-04-07 15:47:10
 */
@Service
public class OrderTblServiceImpl extends ServiceImpl<OrderTblMapper, OrderTblDO>
        implements OrderTblService {

}




