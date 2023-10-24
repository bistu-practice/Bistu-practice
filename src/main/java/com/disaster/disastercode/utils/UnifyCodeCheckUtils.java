package com.disaster.disastercode.utils;

import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

public class UnifyCodeCheckUtils {
    public static void checkCode(String code) {
        baseCheck(code);
        detailCheck(code);
    }

    /**
     * 检查编码是否空白，及长度 36
     */
    public static void baseCheck(String code) {
        if (StringUtils.isBlank(code) || code.length() != 36)
            throw new BusinessException(ErrorCode.CODE_ERROR);
    }

    /**
     * 各项编码的格式检查
     */
    public static void detailCheck(String code) {

        // 载体码 第30位 0-5
        int codeType = Integer.parseInt(code.substring(29, 30));
        if (codeType > 5)
            throw new BusinessException(ErrorCode.CODE_ERROR, "载体编码错误");

        // 时间码 13-26
        String timeCode = code.substring(12, 26);
        int month = Integer.parseInt(timeCode.substring(4, 6));
        int day = Integer.parseInt(timeCode.substring(6, 8));
        int hour = Integer.parseInt(timeCode.substring(8, 10));
        int minus = Integer.parseInt(timeCode.substring(10, 12));
        int second = Integer.parseInt(timeCode.substring(12, 14));
        if (month == 0 || month > 13 || day > 31 || hour > 24 || minus > 60 || second > 60)
            throw new BusinessException(ErrorCode.CODE_ERROR, "时间码格式错误");
        // 载体码大类 27

    }

}
