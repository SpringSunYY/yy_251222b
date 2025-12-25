package com.lz.manage.model.statistics.dto;

import lombok.Data;

/**
 * 统计查询
 *
 * @Project: file
 * @Author: YY
 * @CreateTime: 2025-12-25  20:21
 * @Version: 1.0
 */
@Data
public class StatisticsRequest {
    /**
     * 类型名称
     */
    private String fileTypeName;

    /**
     * 名称
     */
    private String fileName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 用户
     */
    private Long userId;
}
