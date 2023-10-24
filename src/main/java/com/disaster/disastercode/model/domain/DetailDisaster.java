package com.disaster.disastercode.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import com.disaster.disastercode.VO.DetailDisasterForm;
import com.disaster.disastercode.VO.Location;
import lombok.Data;

/**
 * @TableName detail_disaster
 */
@TableName(value = "detail_disaster")
@Data
public class DetailDisaster implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 省、直辖市、自治区行政区划代码
     */
    private String province;

    /**
     * 地市行政区划代码
     */
    private String city;

    /**
     * 县区行政区划代码
     */
    private String county;

    /**
     * 乡镇或街道代码
     */
    private String town;

    /**
     * 行政村或社区代码
     */
    private String village;

    /**
     * 时间，精确到秒
     */
    private Date occurTime;

    /**
     * 来源大类
     */
    private String sourceMain;

    /**
     * 来源子类
     */
    private String sourceSub;

    /**
     * 载体类型
     */
    private String codeType;

    /**
     * 灾情大类
     */
    private String disasterMain;

    /**
     * 灾情子类
     */
    private String disasterSub;

    /**
     * 灾情指标分类
     */
    private String disasterType;

    /**
     * 灾情指标
     */
    private String disasterPoint;

    /**
     * 描述文字
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public DetailDisasterForm formDetailDisasterForm() {
        DetailDisasterForm detailDisasterForm = new DetailDisasterForm();


        Location location = new Location();
        location.setProvince(this.province);
        location.setCity(this.city);
        location.setCounty(this.county);
        location.setTown(this.town);
        location.setVillage(this.village);

        detailDisasterForm.setId(this.id);
        detailDisasterForm.setLocation(location);
        detailDisasterForm.setOccurTime(String.valueOf(this.occurTime));
        detailDisasterForm.setSourceMain(this.sourceMain);
        detailDisasterForm.setSourceSub(this.sourceSub);
        detailDisasterForm.setCodeType(this.codeType);
        detailDisasterForm.setDisasterMain(this.disasterMain);
        detailDisasterForm.setDisasterSub(this.disasterSub);
        detailDisasterForm.setDisasterType(this.disasterType);
        detailDisasterForm.setDisasterPoint(this.disasterPoint);
        detailDisasterForm.setDescription(this.description);

        return detailDisasterForm;
    }
}