package com.disaster.disastercode.service.impl;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.disaster.disastercode.DTO.UploadUrlResponse;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.request.UploadUrlRequest;
import com.disaster.disastercode.service.OssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OssServiceImpl implements OssService {

    @Value("${oss.endPoint}")
    private String endPoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.filePrefix}")
    private String filePrefix;


    private final Long expires = 150L;

    @Bean
    private OSS getOssClient() {
        return new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
    }

    @Override
    public UploadUrlResponse getUploadTempUrl(
            UploadUrlRequest uploadUrlRequest) {
        // 检查参数是否为空
        String code = uploadUrlRequest.getCode();
        String rawFileName = uploadUrlRequest.getFileName();
        String contentType = uploadUrlRequest.getContentType();
        if (StringUtils.isAnyBlank(code, rawFileName, contentType))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "有必须参数为空");
        // 检查文件名是否含非法字符
        if (StringUtils.contains(rawFileName, '/'))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名含/，非法");
        // 处理文件名
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finFileName = code + "-" + simpleDateFormat.format(new Date()) + "-" + rawFileName;
        // 生成签名
        String objectKey = filePrefix + "/" + finFileName;
        GeneratePresignedUrlRequest signedRequest = new GeneratePresignedUrlRequest(bucketName, objectKey, HttpMethod.PUT);
        Date expiration = new Date(new Date().getTime() + expires * 1000);
        signedRequest.setExpiration(expiration);
        signedRequest.setContentType(contentType);

        String downloadFileName;
        try {
            int i = rawFileName.lastIndexOf(".");
            String after = rawFileName.substring(i);
            String front = rawFileName.substring(0, i);
            downloadFileName = URLEncoder.encode(front, "UTF-8") + after;
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.INNER_ERROR, "文件名编码错误");
        }
        String disposition = "attachment;filename="
                + downloadFileName + ";filename*=UTF-8''" + downloadFileName;
        // 获得url返回
        URL signedUrl = getOssClient().generatePresignedUrl(signedRequest);

        UploadUrlResponse uploadUrlResponse = new UploadUrlResponse();
        uploadUrlResponse.setTempUrl(signedUrl.toString());
        uploadUrlResponse.setFileName(finFileName);
        uploadUrlResponse.setDisposition(disposition);
        return uploadUrlResponse;
    }

    @Override
    public UploadUrlResponse getDownloadTempUrl(String fileName) {
        Date expiration = new Date(new Date().getTime() + expires * 1000);
        String finFileName = filePrefix + "/" + fileName;
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, finFileName, HttpMethod.GET);
        request.setExpiration(expiration);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-disposition", "attachment");
        request.setHeaders(headers);
        // 生成签名url
        URL url = getOssClient().generatePresignedUrl(request);
        UploadUrlResponse uploadUrlResponse = new UploadUrlResponse();
        uploadUrlResponse.setTempUrl(url.toString());
        return uploadUrlResponse;
    }

    @Override
    public Boolean isFileExist(String fileName) {
        return getOssClient().doesObjectExist(bucketName, filePrefix + "/" + fileName);
    }

    @Override
    public void deleteFile(String fileName) {
        String finFile = filePrefix + "/" + fileName;
        getOssClient().deleteObject(bucketName, finFile);
    }

}
