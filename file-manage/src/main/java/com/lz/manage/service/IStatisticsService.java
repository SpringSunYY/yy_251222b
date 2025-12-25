package com.lz.manage.service;

import com.lz.manage.model.statistics.dto.StatisticsRequest;
import com.lz.manage.model.statistics.vo.StatisticsVo;

/**
 * 统计
 *
 * @Project: file
 * @Author: YY
 * @CreateTime: 2025-12-25  20:16
 * @Version: 1.0
 */
public interface IStatisticsService {
    /**
     * 浏览记录统计
     * @param statisticsRequest
     * @return
     */
    StatisticsVo viewStatistics(StatisticsRequest statisticsRequest);

    /**
     * 下载记录统计
     * @param statisticsRequest
     * @return
     */
    StatisticsVo downloadStatistics(StatisticsRequest statisticsRequest);
}
