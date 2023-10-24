package com.disaster.disastercode.exception;

import com.disaster.disastercode.common.ErrorCode;

/**
 * 自定义异常类
 * 业务异常，非系统异常
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String description;

    /**
     * 根据错误代码和描述生成错误对象
     */
    public BusinessException(ErrorCode errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
    //无描述
    public BusinessException(ErrorCode errorCode) {
        this(errorCode, "");
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "errorCode=" + errorCode +
                ", description='" + description + '\'' +
                '}';
    }
}