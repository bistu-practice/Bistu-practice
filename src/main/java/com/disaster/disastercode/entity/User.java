package com.disaster.disastercode.entity;

import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * user
 *
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Entity
@Table(name = "user")
public interface User {

    /**
     * 非业务主键,唯一标识用户
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id();

    /**
     * 账户名
     */
    @Key
    String userAccount();

    /**
     * 密码
     */
    String userPassword();

    /**
     * 用户名
     */
    String userName();

    /**
     * 头像链接
     */
    @Nullable
    String avatarUrl();

    /**
     * 职位
     */
    @Nullable
    String userJob();

}
