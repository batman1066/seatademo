package org.example.api.accountapi;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Balance Actions
 */
public interface BalanceSagaApi {

    /**
     * reduce
     *
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     *
     * @param businessKey
     * @param params
     * @return
     */
    boolean compensateReduce(String businessKey, Map<String, Object> params);

}
