package com.lz.manage.model.dto.viewInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.ViewInfo;
/**
 * 浏览记录Vo对象 tb_view_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class ViewInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 类型编号 */
    private Long fileTypeId;

    /** 文件编号 */
    private Long fileId;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param viewInfoInsert 插入对象
     * @return ViewInfoInsert
     */
    public static ViewInfo insertToObj(ViewInfoInsert viewInfoInsert) {
        if (viewInfoInsert == null) {
            return null;
        }
        ViewInfo viewInfo = new ViewInfo();
        BeanUtils.copyProperties(viewInfoInsert, viewInfo);
        return viewInfo;
    }
}
