package com.disaster.disastercode.VO;

import lombok.Data;

/**
 * 行政区划行
 */
@Data
public class LocationLine {
    /**
     * 级别 1 省 2 地市
     * 3 县区 4 乡镇 5 行政村
     */
    int level;
    /**
     * 行政区划代码
     */
    String locationCode;
    /**
     * 行政区划名
     */
    String locationName;
}
