package com.disaster.disastercode.mapper;

import com.disaster.disastercode.DTO.CodeTypeTime;
import com.disaster.disastercode.DTO.MonthTime;
import com.disaster.disastercode.DTO.ProvinceTime;
import com.disaster.disastercode.model.domain.DetailDisaster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 15693
* @description 针对表【detail_disaster】的数据库操作Mapper
* @createDate 2022-11-07 17:21:56
* @Entity com.disaster.disastercode.model.domain.DetailDisaster
*/

public interface DetailDisasterMapper extends BaseMapper<DetailDisaster> {

    /**
     * 获得按年月排序的灾情发生次数
     * @param limits
     * @return
     */
   List<MonthTime> getMonthlyTimes(Integer limits);

    /**
     * 获得所有省份的累计灾情发生次数
     * @return
     */
   List<ProvinceTime> getProvinceTimes();

    /**
     * 获得不同载体类型的灾情次数
     * @return
     */
   List<CodeTypeTime> getCodeTypeTimes();
}




