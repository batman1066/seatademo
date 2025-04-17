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
public class MyDubboProviderExceptionAspect {

    @Around("@within(org.apache.dubbo.config.annotation.DubboService)&&execution( * org.example..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        log.info("MyDubboProviderExceptionAspect来了");
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (RpcException rpcException) {
            log.error("MyDubboProviderExceptionAspect RpcException---", rpcException);
            //被调用者抛过来的，透传
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), rpcException.getMessage());
        } catch (BusinessException e) {
            e.checkAndSetAppName();
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), e.toString());
        } catch (Throwable throwable) {
            log.error("Throwable---", throwable);
            BusinessException businessException = new BusinessException(RespInfo.HTTP_ERROR.getCode(), ExceptionUtils.getRootCauseMessage(throwable), true);
            businessException.checkAndSetAppName();
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), businessException.toString());
        }
        return result;
    }
}