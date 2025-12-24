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
public class ViewInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型编号 */
    private Long fileTypeId;

    /** 文件编号 */
    private Long fileId;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param viewInfoEdit 编辑对象
     * @return ViewInfo
     */
    public static ViewInfo editToObj(ViewInfoEdit viewInfoEdit) {
        if (viewInfoEdit == null) {
            return null;
        }
        ViewInfo viewInfo = new ViewInfo();
        BeanUtils.copyProperties(viewInfoEdit, viewInfo);
        return viewInfo;
    }
}
