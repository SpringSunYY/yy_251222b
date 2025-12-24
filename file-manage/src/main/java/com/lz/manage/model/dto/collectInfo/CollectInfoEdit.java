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
public class CollectInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

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
     * @param collectInfoEdit 编辑对象
     * @return CollectInfo
     */
    public static CollectInfo editToObj(CollectInfoEdit collectInfoEdit) {
        if (collectInfoEdit == null) {
            return null;
        }
        CollectInfo collectInfo = new CollectInfo();
        BeanUtils.copyProperties(collectInfoEdit, collectInfo);
        return collectInfo;
    }
}
