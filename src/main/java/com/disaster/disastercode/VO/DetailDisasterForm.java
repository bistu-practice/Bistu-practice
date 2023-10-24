package com.disaster.disastercode.VO;


import com.disaster.disastercode.model.domain.DetailDisaster;
import lombok.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 详细灾情类
 */
@Data
public class DetailDisasterForm {
    private int id;
    /**
     * 基础地理信息 12位
     */
    private Location location;
    /**
     * 时间码 14位
     */
    private String occurTime;
    /**
     * 来源大类 第27位
     */
    private String sourceMain;
    /**
     * 来源子类 28-29位
     */
    private String sourceSub;
    /**
     * 载体码 30位
     */
    private String codeType;
    /**
     * 灾情大类 31位
     */
    private String disasterMain;
    /**
     * 灾情子类 32-33位
     */
    private String disasterSub;
    /**
     * 灾情大类 34位
     */
    private String disasterType;
    /**
     * 灾情大类 35-36位
     */
    private String disasterPoint;
    /**
     * 描述
     */
    private String description;

    public DetailDisaster formDetailDisaster() {
        DetailDisaster detailDisaster = new DetailDisaster();
        detailDisaster.setId(this.id);
        detailDisaster.setProvince(this.location.getProvince());
        detailDisaster.setCity(this.location.getCity());
        detailDisaster.setCounty(this.location.getCounty());
        detailDisaster.setTown(this.location.getTown());
        detailDisaster.setVillage(this.location.getVillage());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            detailDisaster.setOccurTime(simpleDateFormat.parse(this.occurTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        detailDisaster.setSourceMain(this.sourceMain);
        detailDisaster.setSourceSub(this.sourceSub);
        detailDisaster.setCodeType(this.codeType);
        detailDisaster.setDisasterMain(this.disasterMain);
        detailDisaster.setDisasterSub(this.disasterSub);
        detailDisaster.setDisasterType(this.disasterType);
        detailDisaster.setDisasterPoint(this.disasterPoint);
        detailDisaster.setDescription(this.description);

        return detailDisaster;
    }
}
