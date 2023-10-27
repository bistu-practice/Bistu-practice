package com.disaster.disastercode.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperLogLine {

    private Integer id;

    private String content;

    private Integer businessType;

    private String method;

    private String requestMethod;

    private Integer operationPersonId;

    private String operationPersonName;

    private String operationUrl;

    private String operationIp;

    private Integer status;

    private String errorMsg;

    private Date operationTime;
}
