package com.lz.manage.controller;

import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.manage.model.statistics.dto.StatisticsRequest;
import com.lz.manage.service.IStatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计 Controller
 *
 * @Project: file
 * @Author: YY
 * @CreateTime: 2025-12-25  20:16
 * @Version: 1.0
 */
@RestController
@RequestMapping("/manage/statistics")
public class StatisticsController extends BaseController {
    @Resource
    private IStatisticsService statisticsService;

    /**
     * 浏览记录统计
     */
    @GetMapping("/view")
    @PreAuthorize("@ss.hasPermi('manage:statistics')")
    public AjaxResult viewStatistics(StatisticsRequest statisticsRequest) {
        return success(statisticsService.viewStatistics(statisticsRequest));
    }

    /**
     * 下载记录统计
     */
    @GetMapping("/download")
    @PreAuthorize("@ss.hasPermi('manage:statistics')")
    public AjaxResult downloadStatistics(StatisticsRequest statisticsRequest) {
        return success(statisticsService.downloadStatistics(statisticsRequest));
    }

    /**
     * 收藏记录统计
     */
    @GetMapping("/collect")
    @PreAuthorize("@ss.hasPermi('manage:statistics')")
    public AjaxResult collectStatistics(StatisticsRequest statisticsRequest) {
        return success(statisticsService.collectStatistics(statisticsRequest));
    }
}
