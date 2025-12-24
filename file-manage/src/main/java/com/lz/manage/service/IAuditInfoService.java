package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.AuditInfo;
import com.lz.manage.model.vo.auditInfo.AuditInfoVo;
import com.lz.manage.model.dto.auditInfo.AuditInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 审核信息Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface IAuditInfoService extends IService<AuditInfo>
{
    //region mybatis代码
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
     * 批量删除审核信息
     * 
     * @param ids 需要删除的审核信息主键集合
     * @return 结果
     */
    public int deleteAuditInfoByIds(Long[] ids);

    /**
     * 删除审核信息信息
     * 
     * @param id 审核信息主键
     * @return 结果
     */
    public int deleteAuditInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param auditInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<AuditInfo> getQueryWrapper(AuditInfoQuery auditInfoQuery);

    /**
     * 转换vo
     *
     * @param auditInfoList AuditInfo集合
     * @return AuditInfoVO集合
     */
    List<AuditInfoVo> convertVoList(List<AuditInfo> auditInfoList);

    /**
     * 导入审核信息数据
     *
     * @param auditInfoList 审核信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importAuditInfoData(List<AuditInfo> auditInfoList, Boolean isUpdateSupport, String operName);
}
