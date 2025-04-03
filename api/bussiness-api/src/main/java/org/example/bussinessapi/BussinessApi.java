package org.example.bussinessapi;

import org.example.bussinessapi.dto.PurchaseDTO;

/**
 * BussinessApi
 *
 * @author yangming
 * @date 2025/4/3 16:30
 **/
public interface BussinessApi {
    String purchase(PurchaseDTO dto);
}
