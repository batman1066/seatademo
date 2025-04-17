package org.example.common;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.example.common.constant.BusinessExceptionEnum;

@Data
public class BusinessException extends RuntimeException {

    private int errorCode;
    /**
     * 默认外部错误，展示给用户端
     */
    private Boolean innerException = false;
    /**
     * 发生错误的APP
     */
    private String appName;

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Boolean innerException) {
        super(message);
        this.errorCode = errorCode;
        this.innerException = innerException;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.errorCode = exceptionEnum.getCode();
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum, Boolean innerException) {
        super(exceptionEnum.getMessage());
        this.errorCode = exceptionEnum.getCode();
        this.innerException = innerException;
    }

    @Override
    public String toString() {
        return this.toJsonObject().toJSONString();
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", errorCode);
        jsonObject.put("message", super.getMessage());
        jsonObject.put("innerException", innerException);
        jsonObject.put("appName", appName);
        return jsonObject;
    }

    public void checkAndSetAppName() {
        if (this.getAppName() == null) {
            String projectName = System.getProperty("spring.application.name");
            this.setAppName(projectName);
        }
    }
}