package com.lz.manage.mapper;

import com.lz.manage.model.statistics.dto.StatisticsRequest;
import com.lz.manage.model.statistics.po.StatisticsPo;

import java.util.List;

/**
 * 统计
 *
 * @Project: file
 * @Author: YY
 * @CreateTime: 2025-12-25  20:17
 * @Version: 1.0
 */
public interface StatisticsMapper {
    public List<StatisticsPo> viewStatistics(StatisticsRequest statisticsRequest);
}
