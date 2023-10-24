package com.disaster.disastercode.VO;

import lombok.Data;

@Data
public class Location {
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 镇
     */
    private String town;
    /**
     * 村
     */
    private String village;
}
