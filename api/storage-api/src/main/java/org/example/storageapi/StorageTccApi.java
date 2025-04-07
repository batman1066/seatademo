package org.example.storageapi;

/**
 * StorageApi
 *
 * @author yangming
 * @date 2025/4/3 15:52
 **/
public interface StorageTccApi {
    //tcc
    Boolean prepare(String businessId, String commodityCode, Integer orderCount);

    
}
