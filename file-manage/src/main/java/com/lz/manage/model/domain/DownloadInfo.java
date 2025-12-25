package com.lz.manage.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 下载记录对象 tb_download_info
 *
 * @author YY
 * @date 2025-12-24
 */
@TableName("tb_download_info")
@Data
public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型编号
     */
    @Excel(name = "类型编号")
    private Long fileTypeId;

    /**
     * 类型名称
     */
    @Excel(name = "类型名称")
    private String fileTypeName;

    /**
     * 文件编号
     */
    @Excel(name = "文件编号")
    private Long fileId;

    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    private String fileName;

    /**
     * 文件类型
     */
    @Excel(name = "文件类型")
    private String fileType;

    /**
     * 文件大小
     */
    @Excel(name = "文件大小")
    private Long fileSize;

    /**
     * 文件
     */
    @Excel(name = "文件")
    private String fileUrl;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 用户
     */
    @Excel(name = "用户")
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
