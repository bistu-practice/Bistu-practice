package com.disaster.disastercode.VO;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.disaster.disastercode.utils.UnifyCodeCheckUtils;
import lombok.Data;

@Data
public class UnifyCode {
    /**
     * 一体化编码本身
     */
    @ExcelProperty(value = "id")
    String code;
    /**
     * 文字描述
     */
    @ExcelProperty("description")
    String description;
    /**
     * 载体类型
     * 0 文字 1 图像 2 音频
     * 3 视频 4 其他
     */
    @ExcelIgnore
    int codeType;

    public UnifyCode() {
    }

    /**
     * 除文字其他形式灾情
     */
    public UnifyCode(String code) {
        UnifyCodeCheckUtils.checkCode(code);
        this.code = code;
        this.codeType = Integer.parseInt(code.substring(29, 30));
        this.description = "";
    }

    /**
     * 文字信息灾情
     */
    public UnifyCode(String code, String description) {
        UnifyCodeCheckUtils.checkCode(code);
        this.code = code;
        this.codeType = Integer.parseInt(code.substring(29, 30));
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    @Override
    public String toString() {
        return "UnifyCode{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", codeType=" + codeType +
                '}';
    }
}
