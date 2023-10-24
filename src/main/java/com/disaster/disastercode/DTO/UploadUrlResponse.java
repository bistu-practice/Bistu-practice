package com.disaster.disastercode.DTO;

import lombok.Data;

@Data
public class UploadUrlResponse {
    // 处理后的文件名
    private String fileName;
    // 临时签名地址
    private String tempUrl;
    // 处理后的需要填入的contentDisposition
    private String disposition;
}
