package org.example.storage.service;

import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.transaction.annotation.Transactional;

public interface StorageTccService {
    /*  changeRecord表：
        tcc准备时businessId看修改记录是否存在，存在就是重复执行(幂等)；提交时记录一下即可；取消时执行并记录，
        saga准备时businessId看修改记录是否存在，存在就是重复执行(幂等)或提前取消；取消时看准备记录是否存在，存在就执行并记录，不存在就加一条记录(空回滚和防悬挂)
     */
    @Transactional
    void prepare(String businessId,
                 String commodityCode, Integer orderCount);

    @Transactional
    void commit(BusinessActionContext actionContext);

    @Transactional
    void rollback(BusinessActionContext actionContext);
}
