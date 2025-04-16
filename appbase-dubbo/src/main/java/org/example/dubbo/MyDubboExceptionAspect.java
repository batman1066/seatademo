package org.example.dubbo;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.rpc.RpcException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.common.BusinessException;
import org.example.common.constant.RespInfo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
@Slf4j
public class MyDubboExceptionAspect {

    @Around("@within(org.apache.dubbo.config.annotation.DubboService)&&execution( * org.example..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        log.info("MyDubboExceptionAspect来了");
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (RpcException throwable) {
            log.error("Throwable---", throwable);
            //被调用者抛过来的，透传
            throw throwable;
        } catch (BusinessException e) {
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), e.toString());
        } catch (Throwable throwable) {
            log.error("Throwable---", throwable);
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), ExceptionUtils.getRootCauseMessage(throwable));
        }
        return result;
    }
}