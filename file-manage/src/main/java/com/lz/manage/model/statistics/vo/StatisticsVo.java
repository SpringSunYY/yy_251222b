package com.lz.manage.model.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 统计柱形图VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVo {
   private List<String> names;

    private List<Long> values;
}
