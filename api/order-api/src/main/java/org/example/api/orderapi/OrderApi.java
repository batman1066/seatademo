package org.example.api.orderapi;

/**
 * OrderApi
 *
 * @author yangming
 * @date 2025/4/3 15:51
 **/
public interface OrderApi {

    String create(String userId, String commodityCode,
                  Integer orderCount);
}
