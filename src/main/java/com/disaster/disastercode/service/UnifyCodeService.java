package com.disaster.disastercode.service;

import com.disaster.disastercode.VO.DetailDisasterForm;
import com.disaster.disastercode.VO.UnifyCode;
import org.springframework.web.multipart.MultipartFile;

public interface UnifyCodeService {

    /**
     * 从编码解码出详细信息
     *
     * @param unifyCode 一体化编码类
     * @return 灾情信息类
     */
    DetailDisasterForm getInfoFromCode(UnifyCode unifyCode);

    /**
     * 从excel文件读入文本编码数据
     */
    Boolean insertCodeByExcel(MultipartFile excel);
}
