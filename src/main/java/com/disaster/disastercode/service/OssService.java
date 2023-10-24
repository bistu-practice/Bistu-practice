package com.disaster.disastercode.service;

import com.disaster.disastercode.DTO.UploadUrlResponse;
import com.disaster.disastercode.model.request.UploadUrlRequest;

public interface OssService {


    /**
     * 根据编码，文件名和内容类型生成临时签名url
     * @param uploadUrlRequest
     * @return
     */
    UploadUrlResponse getUploadTempUrl(
            UploadUrlRequest uploadUrlRequest);

    /**
     *  获得下载文件的临时地址
     */
    UploadUrlResponse getDownloadTempUrl(String fileName);
    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    Boolean isFileExist(String fileName);

    /**
     * 删除文件
     */
    void deleteFile(String fileName);
}
