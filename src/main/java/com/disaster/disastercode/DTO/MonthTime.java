package com.disaster.disastercode.DTO;

import lombok.Data;

@Data
public class MonthTime {
    private Integer year;
    private Integer month;
    private Integer times;

    public MonthTime() {
    }

    public MonthTime(Integer year, Integer month) {
        this.year = year;
        this.month = month;
        this.times = 0;
    }

    /**
     * 是否该记录的年月比输入的年月更大
     * 年月相等算true
     * @param year
     * @param month
     * @return
     */
    public boolean compareTimeBigger(int year, int month) {
        if (this.year > year) {
            return true;
        } else if (this.year == year && this.month >= month) {
            return true;
        } else {
            return false;
        }
    }
}
