package org.example.api.bussinessapi;

import org.example.api.bussinessapi.dto.PurchaseDTO;

/**
 * BussinessApi
 *
 * @author yangming
 * @date 2025/4/3 16:30
 **/
public interface BussinessApi {

    String test(String hehe);

    String purchase(PurchaseDTO dto);
}
