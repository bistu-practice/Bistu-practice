package com.disaster.disastercode.service;

import com.disaster.disastercode.model.domain.DetailDisaster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 15693
* @description 针对表【detail_disaster】的数据库操作Service
* @createDate 2022-11-07 17:21:56
*/
public interface DetailDisasterService extends IService<DetailDisaster> {

    Map<String,Object> getStatisticsData(int needMonth);
}
