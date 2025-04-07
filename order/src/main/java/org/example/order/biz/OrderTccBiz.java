package org.example.order.biz;

import org.apache.seata.rm.tcc.api.BusinessActionContext;

public interface OrderTccBiz {

    /**
     * Prepare boolean.
     */
    Boolean prepare(String businessId, String userId, String commodityCode, Integer orderCount);

    /**
     * Commit boolean.
     */
    Boolean commit(BusinessActionContext actionContext);

    /**
     * Rollback boolean.
     */
    Boolean rollback(BusinessActionContext actionContext);
}