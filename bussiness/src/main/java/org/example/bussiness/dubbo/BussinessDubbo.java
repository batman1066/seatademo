package org.example.bussiness.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.api.accountapi.AccountApi;
import org.example.api.bussinessapi.BussinessApi;
import org.example.api.bussinessapi.dto.PurchaseDTO;
import org.example.bussiness.service.BussinessService;
import org.example.common.BusinessException;
import org.example.common.constant.BusinessExceptionEnum;

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
    @DubboReference
    private AccountApi accountApi;

    @Override
    public String test(String hehe) {
        mockError(hehe);
        String result = null;
        try {
            result = accountApi.test(hehe);
        } catch (Exception e) {
            //log.error("BussinessDubbo.test", e);
            throw e;
        }
        return result;
    }

    private void mockError(String hehe) {
        if ("bc".equals(hehe)) {
            int b = 1 / 0;
        } else if ("bbi".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage(), true);
        } else if ("bbo".equals(hehe)) {
            throw new BusinessException(BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getCode(), BusinessExceptionEnum.ACCOUNT_COMPUTE_ERROR.getMessage());
        }
    }


    @Override
    public String purchase(PurchaseDTO dto) {
        if (dto.getTransactionMode().equals("AT")) {
            businessService.purchase(dto);
        } else if (dto.getTransactionMode().equals("TCC")) {
            businessService.tccPurchase(dto);
        } else if (dto.getTransactionMode().equals("SAGA")) {
            businessService.sagaPurchase(dto);
        }
        return "SUCCESS";
    }
}
