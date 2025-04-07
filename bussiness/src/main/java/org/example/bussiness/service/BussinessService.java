package org.example.bussiness.service;

import org.example.bussinessapi.dto.PurchaseDTO;

/**
 * BussinessService
 *
 * @author yangming
 * @date 2025/4/3 16:39
 **/
public interface BussinessService {
    String purchase(PurchaseDTO dto);

    String tccPurchase(PurchaseDTO dto);
}
