package org.example.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.example.common.constant.RespInfo;

//@Component
//@Aspect
//@Order(1)
@Slf4j
public class MyExceptionAspect {

    @Around("execution( * org.example..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable throwable) {
            log.error("Throwable---", throwable);
            String message = throwable.getMessage();
            if (JSON.isValid(message)) {
                JSONObject json = JSON.parseObject(message, JSONObject.class);
                BusinessException businessException = new BusinessException(json.getIntValue("code"), json.getString("msg"));
                throw businessException;
            }
            throw new BusinessException(RespInfo.HTTP_ERROR.getCode(), RespInfo.HTTP_ERROR.getMessage());
        }
        return result;
    }
}