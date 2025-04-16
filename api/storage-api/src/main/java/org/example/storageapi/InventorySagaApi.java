package org.example.storageapi;

/**
 * Inventory Actions
 */
public interface InventorySagaApi {

    /**
     * reduce
     *
     * @param count
     * @return
     */
    boolean reduce(String businessKey, String commodityCode, int count);

    /**
     * increase
     *
     * @return
     */
    boolean compensateReduce(String businessKey);
}
