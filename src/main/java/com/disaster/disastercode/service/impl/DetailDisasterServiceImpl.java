package com.disaster.disastercode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disaster.disastercode.DTO.CodeTypeTime;
import com.disaster.disastercode.DTO.MonthTime;
import com.disaster.disastercode.DTO.ProvinceTime;
import com.disaster.disastercode.model.domain.DetailDisaster;
import com.disaster.disastercode.service.DetailDisasterService;
import com.disaster.disastercode.mapper.DetailDisasterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 15693
 * @description 针对表【detail_disaster】的数据库操作Service实现
 * @createDate 2022-11-07 17:21:56
 */
@Service
public class DetailDisasterServiceImpl extends ServiceImpl<DetailDisasterMapper, DetailDisaster>
        implements DetailDisasterService {

    @Resource
    DetailDisasterMapper detailDisasterMapper;

    @Override
    public Map<String, Object> getStatisticsData(int needMonth) {
        // 近n个月发生灾情的次数
        List<MonthTime> monthlyTimes = detailDisasterMapper.getMonthlyTimes(needMonth);
        List<MonthTime> resMonthlyTimes = selectNeededMonthlyTimes(needMonth, monthlyTimes);
        // 所有省份累计发生灾情的次数
        List<ProvinceTime> provinceTimes = detailDisasterMapper.getProvinceTimes();
        // 不同类别信息载体的灾情比例
        List<CodeTypeTime> codeTypeTimes = detailDisasterMapper.getCodeTypeTimes();
        List<CodeTypeTime> resCodeTypeTimes = formatCodeTypeTimes(codeTypeTimes);

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("monthlyTimes", resMonthlyTimes);
        resMap.put("provinceTimes", provinceTimes);
        resMap.put("codeTypeTimes", resCodeTypeTimes);
        return resMap;
    }

    /**
     * 从候选的每月灾情次数获得最近几个月的灾情次数
     *
     * @param needMonth
     * @param monthlyTimes
     * @return
     */
    private List<MonthTime> selectNeededMonthlyTimes(int needMonth, List<MonthTime> monthlyTimes) {
        ArrayList<MonthTime> resMonthlyTimes = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int monthListIndex = 0;
        for (int i = 0; i < needMonth; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            calendar.add(Calendar.MONTH, -1);
            MonthTime monthTime = new MonthTime(year, month);
            // 已经没有候选的monthList了
            if (monthListIndex + 1 > monthlyTimes.size()) {
                resMonthlyTimes.add(monthTime);
                continue;
            }
            // 与目前的结果比较
            if (monthlyTimes.get(monthListIndex).compareTimeBigger(year, month)) {
                // 时间对应,可填入
                resMonthlyTimes.add(monthlyTimes.get(monthListIndex));
                monthListIndex++;
            } else {
                // 时间过小
                resMonthlyTimes.add(monthTime);
            }
        }
        return resMonthlyTimes;
    }

    /**
     * 填充空缺的载体类型
     *
     * @param codeTypeTimes
     * @return
     */
    private List<CodeTypeTime> formatCodeTypeTimes(List<CodeTypeTime> codeTypeTimes) {
        List<String> codeTypes = Arrays.asList("文字", "图像", "音频", "视频", "其他");
        ArrayList<CodeTypeTime> resCodeTypeTimes = new ArrayList<>();
        for (String codeType : codeTypes) {
            CodeTypeTime codeTypeTime = codeTypeTimes.stream().filter(c -> c.getCodeType().equals(codeType)).findFirst().orElse(null);
            if (codeTypeTime != null) {
                resCodeTypeTimes.add(codeTypeTime);
            } else {
                CodeTypeTime typeTime = new CodeTypeTime();
                typeTime.setCodeType(codeType);
                typeTime.setTimes(0);
                resCodeTypeTimes.add(typeTime);
            }
        }
        return resCodeTypeTimes;
    }
}




