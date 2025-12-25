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
 * 审核信息对象 tb_audit_info
 *
 * @author YY
 * @date 2025-12-24
 */
@TableName("tb_audit_info")
@Data
public class AuditInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private Long userId;

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
     * 审核人
     */
    @Excel(name = "审核人")
    private Long auditUserId;

    /**
     * 审核内容
     */
    @Excel(name = "审核内容")
    private String content;

    /**
     * 是否同意
     */
    @Excel(name = "是否同意", dictType = "is_agree")
    private String isAgree;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
