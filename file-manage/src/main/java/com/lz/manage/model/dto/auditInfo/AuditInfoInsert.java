package com.lz.manage.model.dto.auditInfo;

import com.lz.manage.model.domain.AuditInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 审核信息Vo对象 tb_audit_info
 *
 * @author YY
 * @date 2025-12-25
 */
@Data
public class AuditInfoInsert implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件编号
     */
    private Long fileId;

    /**
     * 申请理由
     */
    private String applyReason;

    /**
     * 审核内容
     */
    private String content;

    /**
     * 是否同意
     */
    private String isAgree;

    /**
     * 对象转封装类
     *
     * @param auditInfoInsert 插入对象
     * @return AuditInfoInsert
     */
    public static AuditInfo insertToObj(AuditInfoInsert auditInfoInsert) {
        if (auditInfoInsert == null) {
            return null;
        }
        AuditInfo auditInfo = new AuditInfo();
        BeanUtils.copyProperties(auditInfoInsert, auditInfo);
        return auditInfo;
    }
}
