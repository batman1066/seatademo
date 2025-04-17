package org.example.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.common.constant.RespInfo;
import org.springframework.util.StringUtils;

public class BusinessExceptionUtil {
    /**
     * translateToBusinessException
     *
     * @param ex
     * @return
     */
    public static BusinessException translateToBusinessException(Exception ex) {
        if (ex instanceof BusinessException) {
            return (BusinessException) ex;
        }
        String messagePrefix = "java.util.concurrent.ExecutionException: org.apache.dubbo.rpc.RpcException:";
        String message = ex.getMessage();
        if (StringUtils.hasText(message)) {
            message = message.replace(messagePrefix, "");
        }
        BusinessException businessException;
        if (JSON.isValid(message)) {
            JSONObject json = JSON.parseObject(message, JSONObject.class);
            businessException = new BusinessException(json.getIntValue("errorCode"), json.getString("message"), json.getBooleanValue("innerException"));
            businessException.setAppName(json.getString("appName"));
        } else {
            businessException = new BusinessException(RespInfo.HTTP_ERROR.getCode(), message, true);
        }
        return businessException;
    }
}
