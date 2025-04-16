package org.example.common.constant;

public enum BusinessExceptionEnum {

    ACCOUNT_COMPUTE_ERROR(100001, "账务计算错误");

    private final int code;
    private final String message;

    BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
