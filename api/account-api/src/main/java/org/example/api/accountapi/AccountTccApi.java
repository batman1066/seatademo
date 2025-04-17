package org.example.api.accountapi;


/**
 * AccountApi
 *
 * @author yangming
 * @date 2025/3/31 15:34
 **/
public interface AccountTccApi {
    //tcc
    Boolean prepare(String businessId, String userId, Integer money);

}
