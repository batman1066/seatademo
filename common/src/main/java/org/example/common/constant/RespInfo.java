package org.example.common.constant;

public enum RespInfo {
    HTTP_OK(200, "success"),
    HTTP_ERROR(500, "error");
    private final int code;
    private final String message;

    RespInfo(int code, String message) {
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
