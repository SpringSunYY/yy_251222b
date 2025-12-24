package com.lz.manage.model.dto.collectInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.CollectInfo;
/**
 * 收藏记录Vo对象 tb_collect_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 收藏夹编号 */
    private Long folderId;

    /** 类型编号 */
    private Long fileTypeId;

    /** 文件编号 */
    private Long fileId;

    /** 用户 */
    private Long userId;

    /**
     * 对象转封装类
     *
     * @param collectInfoInsert 插入对象
     * @return CollectInfoInsert
     */
    public static CollectInfo insertToObj(CollectInfoInsert collectInfoInsert) {
        if (collectInfoInsert == null) {
            return null;
        }
        CollectInfo collectInfo = new CollectInfo();
        BeanUtils.copyProperties(collectInfoInsert, collectInfo);
        return collectInfo;
    }
}
