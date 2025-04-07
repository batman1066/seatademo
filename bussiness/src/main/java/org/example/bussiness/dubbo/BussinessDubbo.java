package org.example.bussiness.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.bussiness.service.BussinessService;
import org.example.bussinessapi.BussinessApi;
import org.example.bussinessapi.dto.PurchaseDTO;

import javax.annotation.Resource;

/**
 * BussinessDubbo
 *
 * @author yangming
 * @date 2025/4/3 16:27
 **/
@DubboService
@Slf4j
public class BussinessDubbo implements BussinessApi {
    @Resource
    private BussinessService businessService;

    @Override
    public String purchase(PurchaseDTO dto) {
        if (dto.getTransactionMode().equals("AT")) {
            businessService.purchase(dto);
        } else if (dto.getTransactionMode().equals("TCC")) {
            businessService.tccPurchase(dto);
        } else if (dto.getTransactionMode().equals("SAGA")) {
            businessService.purchase(dto);
        }
        return "SUCCESS";
    }
}
