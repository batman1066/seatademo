package org.example.accountapi;

/**
 * AccountApi
 *
 * @author yangming
 * @date 2025/3/31 15:34
 **/
public interface AccountApi {
    String test(String hehe);
    
    String debit(String userId, Integer money);

}
