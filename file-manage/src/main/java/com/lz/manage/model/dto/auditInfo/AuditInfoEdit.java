package com.lz.manage.model.dto.auditInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.AuditInfo;
/**
 * 审核信息Vo对象 tb_audit_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class AuditInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型编号 */
    private Long fileTypeId;

    /** 文件编号 */
    private Long fileId;

    /** 审核内容 */
    private String content;

    /** 是否同意 */
    private String isAgree;

    /**
     * 对象转封装类
     *
     * @param auditInfoEdit 编辑对象
     * @return AuditInfo
     */
    public static AuditInfo editToObj(AuditInfoEdit auditInfoEdit) {
        if (auditInfoEdit == null) {
            return null;
        }
        AuditInfo auditInfo = new AuditInfo();
        BeanUtils.copyProperties(auditInfoEdit, auditInfo);
        return auditInfo;
    }
}
