package com.disaster.disastercode.model.domain;

import com.disaster.disastercode.VO.OperLogLine;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.io.Serializable;

/**
 * (Operlog)实体类
 *
 * @author makejava
 * @since 2023-10-26 20:30:11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OperLog implements Serializable {
    private static final long serialVersionUID = 589870479595357240L;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    public OperLogLine formOperLogLine(OperLog log){
//        return OperLogLine.builder()
//                .businessType(this.businessType)
//                .content(content)
//                .errorMsg(errorMsg)
//                .method(method)
//                .operationPersonId(operationPersonId)
//                .operationPersonName(operationPersonName)
//                .operationTime(operationTime)
//                .operationUrl(operationUrl)
//                .id(id)
//                .requestMethod(requestMethod)
//                .status(status)
//                .operationIp(operationIp)
//                .build();
        OperLogLine operLogLine = new OperLogLine();
        operLogLine.setOperationIp(this.operationIp);
        operLogLine.setOperationPersonName(operationPersonName);
        operLogLine.setId(id);
        operLogLine.setOperationPersonId(operationPersonId);
        operLogLine.setContent(content);
        operLogLine.setOperationUrl(operationUrl);
        operLogLine.setOperationTime(operationTime);
        operLogLine.setMethod(method);
        return operLogLine;
    }

}

