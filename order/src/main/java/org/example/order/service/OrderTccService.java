package org.example.order.service;

import org.apache.seata.rm.tcc.api.BusinessActionContext;

public interface OrderTccService {

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