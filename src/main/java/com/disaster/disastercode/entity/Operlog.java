package com.disaster.disastercode.entity;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * operlog
 *
 * </p>
 *
 * @author Sun
 * @date 2023-10-28
 */
@Entity
@Table(name = "operlog")
public interface Operlog {

    /**
     * 非业务主键,唯一标识一条日志记录
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id();

    /**
     * content
     */
    @Nullable
    String content();

    /**
     * businessType
     */
    @Nullable
    Integer businessType();

    /**
     * method
     */
    @Nullable
    String method();

    /**
     * requestMethod
     */
    @Nullable
    String requestMethod();

    /**
     * operationPersonId
     */
    @Nullable
    Integer operationPersonId();

    /**
     * operationPersonName
     */
    @Nullable
    String operationPersonName();

    /**
     * operationUrl
     */
    @Nullable
    String operationUrl();

    /**
     * operationIp
     */
    @Nullable
    String operationIp();

    /**
     * status
     */
    @Nullable
    Integer status();

    /**
     * errorMsg
     */
    @Nullable
    String errorMsg();

    /**
     * operationTime
     */
    @Nullable
    LocalDateTime operationTime();

}
