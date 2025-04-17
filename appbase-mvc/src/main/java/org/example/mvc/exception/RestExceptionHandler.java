package org.example.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.example.common.BusinessException;
import org.example.common.BusinessExceptionUtil;
import org.example.common.RespBean;
import org.example.common.constant.RespInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    public static final String SYSTEM_ERROR_MSG = "内部服务器错误";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RespBean> handleBusinessException(BusinessException bEx) {
        log.error("BusinessException:", bEx);
        //封装返回给前端
        RespBean error = RestExceptionHandler.businessExceptionToRespBean(bEx);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespBean> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:", ex);
        StringBuffer errorMsg = new StringBuffer();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorMsg.append(((FieldError) error).getField() + error.getDefaultMessage());
        });
        BusinessException businessException = new BusinessException(RespInfo.HTTP_ERROR.getCode(), errorMsg.toString());
        //封装返回给前端
        RespBean errors = RestExceptionHandler.businessExceptionToRespBean(businessException);
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(RpcException.class)
    public ResponseEntity<RespBean> handleRpcException(Exception ex) {
        log.error("RpcException:", ex);
        BusinessException businessException = BusinessExceptionUtil.translateToBusinessException(ex);
        //封装返回给前端
        RespBean error = RestExceptionHandler.businessExceptionToRespBean(businessException);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespBean> handleGeneralException(Exception ex) {
        log.error("Exception:", ex);
        BusinessException businessException = BusinessExceptionUtil.translateToBusinessException(ex);
        //封装返回给前端
        RespBean error = RestExceptionHandler.businessExceptionToRespBean(businessException);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }


    /**
     * businessExceptionToRespBean
     *
     * @param bEx
     * @return
     */
    public static RespBean businessExceptionToRespBean(BusinessException bEx) {
        bEx.checkAndSetAppName();
        RespBean error;
        //封装返回给前端
        if (bEx.getInnerException() != null && !bEx.getInnerException()) {
            //不是内部异常，全给前端
            error = new RespBean(bEx.toJsonObject(), bEx.getErrorCode(), bEx.getMessage());
        } else {
            //是内部异常，不给前端
            error = new RespBean(bEx.toJsonObject(), RespInfo.HTTP_ERROR.getCode(), SYSTEM_ERROR_MSG);
        }
        return error;
    }

}
