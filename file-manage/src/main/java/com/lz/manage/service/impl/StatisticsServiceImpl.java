package com.lz.manage.service.impl;

import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.manage.mapper.StatisticsMapper;
import com.lz.manage.model.statistics.dto.StatisticsRequest;
import com.lz.manage.model.statistics.po.StatisticsPo;
import com.lz.manage.model.statistics.vo.StatisticsVo;
import com.lz.manage.service.IStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 统计
 *
 * @Project: file
 * @Author: YY
 * @CreateTime: 2025-12-25  20:17
 * @Version: 1.0
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {
    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public StatisticsVo viewStatistics(StatisticsRequest statisticsRequest) {
        //初始查询条件
        initDate(statisticsRequest);
        List<StatisticsPo> pos = statisticsMapper.viewStatistics(statisticsRequest);
        StatisticsVo statisticsVo = new StatisticsVo();
        List<String> names = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        for (StatisticsPo po : pos) {
            names.add(po.getName());
            values.add(po.getValue());
        }
        statisticsVo.setNames(names);
        statisticsVo.setValues(values);
        return statisticsVo;
    }

    @Override
    public StatisticsVo downloadStatistics(StatisticsRequest statisticsRequest) {
        initDate(statisticsRequest);
        List<StatisticsPo> pos = statisticsMapper.downloadStatistics(statisticsRequest);
        StatisticsVo statisticsVo = new StatisticsVo();
        List<String> names = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        for (StatisticsPo po : pos) {
            names.add(po.getName());
            values.add(po.getValue());
        }
        statisticsVo.setNames(names);
        statisticsVo.setValues(values);
        return statisticsVo;
    }

    @Override
    public StatisticsVo collectStatistics(StatisticsRequest statisticsRequest) {
        initDate(statisticsRequest);
        List<StatisticsPo> pos = statisticsMapper.collectStatistics(statisticsRequest);
        StatisticsVo statisticsVo = new StatisticsVo();
        List<String> names = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        for (StatisticsPo po : pos) {
            names.add(po.getName());
            values.add(po.getValue());
        }
        statisticsVo.setNames(names);
        statisticsVo.setValues(values);
        return statisticsVo;
    }

    private void initDate(StatisticsRequest statisticsRequest) {
        //如果开始时间或者结束时间某一个为空
        if (StringUtils.isEmpty(statisticsRequest.getStartTime())
                || StringUtils.isEmpty(statisticsRequest.getEndTime())) {
            //开始时间为七天前
            Date endTime = DateUtils.getNowDate();
            Date startTime = DateUtils.addDays(endTime, -7);
            String startTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, startTime);
            String endTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, endTime);
            statisticsRequest.setEndTime(endTimeStr);
            statisticsRequest.setStartTime(startTimeStr);
        }
        //如果没有权限
        if (SecurityUtils.hasPermi("manage:statistics:all")) {
            statisticsRequest.setUserId(SecurityUtils.getUserId());
        }
    }
}
