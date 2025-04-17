package org.example.bussiness.service;

import org.example.api.bussinessapi.dto.PurchaseDTO;

/**
 * BussinessService
 *
 * @author yangming
 * @date 2025/4/3 16:39
 **/
public interface BussinessService {
    //XA AT 调整yaml配置
    String purchase(PurchaseDTO dto);

    String tccPurchase(PurchaseDTO dto);

    String sagaPurchase(PurchaseDTO dto);
}
