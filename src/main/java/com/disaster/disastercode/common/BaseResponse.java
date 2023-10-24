package com.disaster.disastercode.common;

import com.disaster.disastercode.exception.BusinessException;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用的请求返回类
 * @param <T> data所属类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5403793635590510571L;

    private int code; //代码 为0时成功
    private String message; //信息
    private T data; //数据 可能为null

    private String description; //描述

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = "";
    }
    //无message情况
    public BaseResponse(int code, T data) {
        this(code, "", data);
    }
    //由错误码生成，无data
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }
    //错误码及详细描述
    public BaseResponse(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
        this.description = description;
    }
    //由业务错误生成
    public BaseResponse(BusinessException businessException) {
        this(businessException.getErrorCode(), businessException.getDescription());
    }
}