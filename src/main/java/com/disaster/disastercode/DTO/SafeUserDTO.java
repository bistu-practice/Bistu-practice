package com.disaster.disastercode.DTO;

import lombok.Data;
@Data
public class SafeUserDTO {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 用户职位
     */
    private String userJob;
}
