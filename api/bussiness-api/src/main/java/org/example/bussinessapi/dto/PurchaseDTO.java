package org.example.bussinessapi.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * PurchaseDTO
 *
 * @author yangming
 * @date 2025/4/3 16:41
 **/
@Data
public class PurchaseDTO implements Serializable {
    /**
     * userId – 用户ID
     * commodityCode – 商品编号
     * orderCount – 订购数量
     */
    String userId;
    String commodityCode;
    Integer orderCount;
    String transactionMode;

}
