package org.example.dubbo;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.rpc.RpcException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.common.BusinessException;
import org.example.common.BusinessExceptionUtil;
import org.example.common.constant.RespInfo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
@Slf4j
public class MyDubboConsumerExceptionAspect {

    @Around("!@within(org.apache.dubbo.config.annotation.DubboService)&&execution( * org.example.api..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        log.info("MyDubboConsumerExceptionAspect来了");
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (RpcException rpcException) {
            log.error("MyDubboConsumerExceptionAspect RpcException---", rpcException);
            //还原为BusinessException
            BusinessException businessException = BusinessExceptionUtil.translateToBusinessException(rpcException);
            //被调用者抛过来的，还原后抛出
            throw businessException;
        } catch (Throwable throwable) {
            log.error("MyDubboConsumerExceptionAspect Throwable---", throwable);
            BusinessException businessException = new BusinessException(RespInfo.HTTP_ERROR.getCode(), ExceptionUtils.getRootCauseMessage(throwable), true);
            businessException.checkAndSetAppName();
            throw new RpcException(RespInfo.HTTP_ERROR.getCode(), businessException.toString());
        }
        return result;
    }
}