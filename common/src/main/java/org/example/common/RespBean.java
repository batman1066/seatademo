package org.example.common;

import lombok.Data;
import org.example.common.constant.RespInfo;

import java.io.Serializable;

@Data
public class RespBean<T> implements Serializable {

    private static final long serialVersionUID = 7048691672612601L;


    /**
     * 状态码
     */
    private int code;

    /**
     * 信息对象
     */
    private String msg;

    /**
     * 追踪信息 json
     */
    private Object traceMsg;

    /**
     * 结果对象
     */
    private T data;

    public RespBean() {
        this(RespInfo.HTTP_OK.getCode(), RespInfo.HTTP_OK.getMessage(), null);
    }


    public RespBean(int code, String msg) {
        this(code, msg, null);
    }

    public RespBean(Object traceMsg, int code, String msg) {
        this.traceMsg = traceMsg;
        this.code = code;
        this.msg = msg;
    }

    public RespBean(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> RespBean<T> ok() {
        return new RespBean<>(RespInfo.HTTP_OK.getCode(), RespInfo.HTTP_OK.getMessage());
    }

    public static <T> RespBean<T> ok(T data) {
        return new RespBean<>(RespInfo.HTTP_OK.getCode(), RespInfo.HTTP_OK.getMessage(), data);
    }
}