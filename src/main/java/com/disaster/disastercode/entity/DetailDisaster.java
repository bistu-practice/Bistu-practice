package com.disaster.disastercode.entity;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * detail_disaster
 *
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Entity
@Table(name = "detail_disaster")
public interface DetailDisaster {

    /**
     * 非业务主键,唯一标识一次灾情
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id();

    /**
     * 省名称
     */
    String province();

    /**
     * 城市名
     */
    String city();

    /**
     * 区域名
     */
    String county();

    /**
     * 街道名
     */
    String town();

    /**
     * 村庄名
     */
    String village();

    /**
     * 发生时间
     */
    LocalDateTime occurTime();

    /**
     * 来源大类
     */
    @Nullable
    String sourceMain();

    /**
     * 来源小类
     */
    @Nullable
    String sourceSub();

    /**
     * 载体类别
     */
    @Nullable
    String codeType();

    /**
     * 灾情大类
     */
    @Nullable
    String disasterMain();

    /**
     * 灾情小类
     */
    @Nullable
    String disasterSub();

    /**
     * 损失类型
     */
    @Nullable
    String disasterType();

    /**
     * 损失项目
     */
    @Nullable
    String disasterPoint();

    /**
     * 描述内容
     */
    @Nullable
    String description();

}
