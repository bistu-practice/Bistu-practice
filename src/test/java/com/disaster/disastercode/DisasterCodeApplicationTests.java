package com.disaster.disastercode;

import com.disaster.disastercode.DTO.UploadUrlResponse;
import com.disaster.disastercode.VO.DetailDisasterForm;
import com.disaster.disastercode.VO.UnifyCode;
import com.disaster.disastercode.model.request.UploadUrlRequest;
import com.disaster.disastercode.service.DetailDisasterService;
import com.disaster.disastercode.service.OssService;
import com.disaster.disastercode.service.UnifyCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DisasterCodeApplicationTests {
    @Resource
    private UnifyCodeService unifyCodeService;
    @Resource
    private OssService ossService;
    @Resource
    private DetailDisasterService detailDisasterService;
    @Test
    void contextLoads() {
    }

    //@Test
    void testLocationCode() {
        UnifyCode unifyCode = new UnifyCode("632626200206202105220204001010302001", "地震啦");
        DetailDisasterForm infoFromCode = unifyCodeService.getInfoFromCode(unifyCode);
        System.out.println(infoFromCode);
    }

    //@Test
    void testOSS() {
        UploadUrlRequest uploadUrlRequest = new UploadUrlRequest();
        uploadUrlRequest.setCode("1");
        uploadUrlRequest.setFileName("1.txt");
        uploadUrlRequest.setContentType("text/plain");
        UploadUrlResponse uploadTempUrl = ossService.getUploadTempUrl(uploadUrlRequest);
        System.out.println(uploadTempUrl);
        System.out.println(System.currentTimeMillis());
        System.out.println(ossService.isFileExist("a.txt"));
        System.out.println(System.currentTimeMillis());
    }

    //@Test
    void testStatisticsData() {
        detailDisasterService.getStatisticsData(12);
    }

}
