package com.lz.manage.model.dto.auditInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.manage.model.domain.AuditInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 审核信息Query对象 tb_audit_info
 *
 * @author YY
 * @date 2025-12-25
 */
@Data
public class AuditInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 类型名称
     */
    private String fileTypeName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件
     */
    private String fileUrl;

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
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param auditInfoQuery 查询对象
     * @return AuditInfo
     */
    public static AuditInfo queryToObj(AuditInfoQuery auditInfoQuery) {
        if (auditInfoQuery == null) {
            return null;
        }
        AuditInfo auditInfo = new AuditInfo();
        BeanUtils.copyProperties(auditInfoQuery, auditInfo);
        return auditInfo;
    }
}
