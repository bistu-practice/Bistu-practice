package com.disaster.disastercode.common;

/**
 * 错误码 枚举类
 */
public enum ErrorCode {
    METHOD_NOT_ALLOWED(405, "请求的方法不支持"),
    PARAMS_ERROR(40000, "请求参数错误"),
    PARAMS_NULL_ERROR(40001, "请求参数为空"),

    NOT_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    LOGIN_FAIL(40102, "登录失败"),
    TOKEN_EXPIRE(40103, "token已过期"),
    TOKEN_FAIL(40104, "token验证失败"),

    INNER_ERROR(50000, "服务器内部错误"),

    CODE_ERROR(60001, "编码格式错误");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}