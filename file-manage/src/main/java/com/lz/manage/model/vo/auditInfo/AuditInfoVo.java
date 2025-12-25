package com.lz.manage.model.vo.auditInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.AuditInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核信息Vo对象 tb_audit_info
 *
 * @author YY
 * @date 2025-12-25
 */
@Data
public class AuditInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;
    private String userName;

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
     * 申请理由
     */
    private String applyReason;

    /**
     * 审核人
     */
    private Long auditUserId;
    private String auditUserName;

    /**
     * 审核内容
     */
    private String content;

    /**
     * 是否同意
     */
    private String isAgree;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date auditTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;


    /**
     * 对象转封装类
     *
     * @param auditInfo AuditInfo实体对象
     * @return AuditInfoVo
     */
    public static AuditInfoVo objToVo(AuditInfo auditInfo) {
        if (auditInfo == null) {
            return null;
        }
        AuditInfoVo auditInfoVo = new AuditInfoVo();
        BeanUtils.copyProperties(auditInfo, auditInfoVo);
        return auditInfoVo;
    }
}
