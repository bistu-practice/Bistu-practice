package com.disaster.disastercode.exception;

import com.disaster.disastercode.common.BaseResponse;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Null> businessExceptionHandler(BusinessException e) {
        log.error("已处理异常", e);
        return ResultUtils.error(e);
    }
    //处理内部错误
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Null> runtimeExceptionHandler(RuntimeException e) {
        log.error("服务器错误", e);
        return ResultUtils.error(ErrorCode.INNER_ERROR);
    }
    //处理方法不支持错误
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<Null> methodNotAllowedHandler(HttpRequestMethodNotSupportedException e) {
        log.error("方法不支持",e);
        return ResultUtils.error(ErrorCode.METHOD_NOT_ALLOWED);
    }
}
