package org.example.account.dubbo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.accountapi.BalanceSagaApi;

import java.math.BigDecimal;
import java.util.Map;

@DubboService
@Slf4j
public class BalanceSagaDubbo implements BalanceSagaApi {

    @Override
    public boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params) {
       /* if (params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("reduce balance failed");
            }
        }*/
        log.info("reduce balance succeed, params: " + JSON.toJSONString(params));
        log.info("reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey, Map<String, Object> params) {
       /* if (params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("compensate reduce balance failed");
            }
        }*/
        log.info("compensate reduce balance succeed, businessKey:" + businessKey);
        return true;
    }
}