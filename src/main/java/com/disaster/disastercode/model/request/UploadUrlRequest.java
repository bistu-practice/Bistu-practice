package com.disaster.disastercode.model.request;

import lombok.Data;

@Data
public class UploadUrlRequest {
    String code;
    String fileName;
    String contentType;
}
