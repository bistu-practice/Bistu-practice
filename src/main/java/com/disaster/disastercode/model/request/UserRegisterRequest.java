package com.disaster.disastercode.model.request;

import lombok.Data;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest {
    private String userAccount;
    private String userPassword;
    private String userName;
}
