package org.example.common;

import com.alibaba.fastjson.JSONObject;
import org.example.common.constant.BusinessExceptionEnum;


public class BusinessException extends RuntimeException {
    private int errorCode;

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.errorCode = exceptionEnum.getCode();
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", errorCode);
        jsonObject.put("message", super.getMessage());
        return jsonObject.toJSONString();
    }
}