package com.lz.manage.model.vo.viewInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.ViewInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 浏览记录Vo对象 tb_view_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class ViewInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 类型编号
     */
    private Long fileTypeId;

    /**
     * 类型名称
     */
    private String fileTypeName;

    /**
     * 文件编号
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件
     */
    private String fileUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 对象转封装类
     *
     * @param viewInfo ViewInfo实体对象
     * @return ViewInfoVo
     */
    public static ViewInfoVo objToVo(ViewInfo viewInfo) {
        if (viewInfo == null) {
            return null;
        }
        ViewInfoVo viewInfoVo = new ViewInfoVo();
        BeanUtils.copyProperties(viewInfo, viewInfoVo);
        return viewInfoVo;
    }
}
