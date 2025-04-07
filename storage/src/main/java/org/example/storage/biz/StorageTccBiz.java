package org.example.storage.biz;

import org.apache.seata.rm.tcc.api.BusinessActionContext;

public interface StorageTccBiz {
    /**
     * Prepare boolean.
     */
    Boolean prepare(String businessId, String commodityCode, Integer orderCount);

    /**
     * Commit boolean.
     */
    Boolean commit(BusinessActionContext actionContext);

    /**
     * Rollback boolean.
     */
    Boolean rollback(BusinessActionContext actionContext);

}