package org.example.orderapi;


/**
 * OrderApi
 *
 * @author yangming
 * @date 2025/4/3 15:51
 **/
public interface OrderTccApi {
    //tcc
    Boolean prepare(String businessId, String userId, String commodityCode, Integer orderCount);

}
