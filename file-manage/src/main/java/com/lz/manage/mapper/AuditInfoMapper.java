package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.AuditInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 审核信息Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface AuditInfoMapper extends BaseMapper<AuditInfo>
{
    /**
     * 查询审核信息
     * 
     * @param id 审核信息主键
     * @return 审核信息
     */
    public AuditInfo selectAuditInfoById(Long id);

    /**
     * 查询审核信息列表
     * 
     * @param auditInfo 审核信息
     * @return 审核信息集合
     */
    public List<AuditInfo> selectAuditInfoList(AuditInfo auditInfo);

    /**
     * 新增审核信息
     * 
     * @param auditInfo 审核信息
     * @return 结果
     */
    public int insertAuditInfo(AuditInfo auditInfo);

    /**
     * 修改审核信息
     * 
     * @param auditInfo 审核信息
     * @return 结果
     */
    public int updateAuditInfo(AuditInfo auditInfo);

    /**
     * 删除审核信息
     * 
     * @param id 审核信息主键
     * @return 结果
     */
    public int deleteAuditInfoById(Long id);

    /**
     * 批量删除审核信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAuditInfoByIds(Long[] ids);
}
