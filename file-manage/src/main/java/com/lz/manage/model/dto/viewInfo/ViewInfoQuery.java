package com.lz.manage.model.dto.viewInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lz.manage.model.domain.ViewInfo;
/**
 * 浏览记录Query对象 tb_view_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class ViewInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型名称 */
    private String fileTypeName;

    /** 文件名称 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param viewInfoQuery 查询对象
     * @return ViewInfo
     */
    public static ViewInfo queryToObj(ViewInfoQuery viewInfoQuery) {
        if (viewInfoQuery == null) {
            return null;
        }
        ViewInfo viewInfo = new ViewInfo();
        BeanUtils.copyProperties(viewInfoQuery, viewInfo);
        return viewInfo;
    }
}
