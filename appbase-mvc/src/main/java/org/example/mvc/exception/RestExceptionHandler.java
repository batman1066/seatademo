package org.example.mvc.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.common.BusinessException;
import org.example.common.RespBean;
import org.example.common.constant.RespInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RespBean> handleBusinessException(BusinessException ex) {
        log.error("BusinessException:", ex);
        RespBean error = new RespBean(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespBean> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:", ex);
        RespBean errors = new RespBean();
        StringBuffer errorMsg = new StringBuffer();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorMsg.append(((FieldError) error).getField() + error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespBean> handleGeneralException(Exception ex) {
        log.error("Exception:", ex);
        String message = ex.getMessage();
        RespBean error = null;
        if (JSON.isValid(message)) {
            JSONObject json = JSON.parseObject(message, JSONObject.class);
            error = new RespBean(json.getIntValue("code"), json.getString("msg"));
        } else {
            error = new RespBean(RespInfo.HTTP_ERROR.getCode(), "内部服务器错误");
        }
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
