package com.lz.manage.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lz.common.utils.StringUtils;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.manage.mapper.AuditInfoMapper;
import com.lz.manage.model.domain.AuditInfo;
import com.lz.manage.service.IAuditInfoService;
import com.lz.manage.model.dto.auditInfo.AuditInfoQuery;
import com.lz.manage.model.vo.auditInfo.AuditInfoVo;

/**
 * 审核信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class AuditInfoServiceImpl extends ServiceImpl<AuditInfoMapper, AuditInfo> implements IAuditInfoService
{
    private static final Logger log = LoggerFactory.getLogger(AuditInfoServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private AuditInfoMapper auditInfoMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询审核信息
     *
     * @param id 审核信息主键
     * @return 审核信息
     */
    @Override
    public AuditInfo selectAuditInfoById(Long id)
    {
        return auditInfoMapper.selectAuditInfoById(id);
    }

    /**
     * 查询审核信息列表
     *
     * @param auditInfo 审核信息
     * @return 审核信息
     */
    @Override
    public List<AuditInfo> selectAuditInfoList(AuditInfo auditInfo)
    {
        return auditInfoMapper.selectAuditInfoList(auditInfo);
    }

    /**
     * 新增审核信息
     *
     * @param auditInfo 审核信息
     * @return 结果
     */
    @Override
    public int insertAuditInfo(AuditInfo auditInfo)
    {
        auditInfo.setCreateTime(DateUtils.getNowDate());
        return auditInfoMapper.insertAuditInfo(auditInfo);
    }

    /**
     * 修改审核信息
     *
     * @param auditInfo 审核信息
     * @return 结果
     */
    @Override
    public int updateAuditInfo(AuditInfo auditInfo)
    {
        auditInfo.setUpdateTime(DateUtils.getNowDate());
        return auditInfoMapper.updateAuditInfo(auditInfo);
    }

    /**
     * 批量删除审核信息
     *
     * @param ids 需要删除的审核信息主键
     * @return 结果
     */
    @Override
    public int deleteAuditInfoByIds(Long[] ids)
    {
        return auditInfoMapper.deleteAuditInfoByIds(ids);
    }

    /**
     * 删除审核信息信息
     *
     * @param id 审核信息主键
     * @return 结果
     */
    @Override
    public int deleteAuditInfoById(Long id)
    {
        return auditInfoMapper.deleteAuditInfoById(id);
    }
    //endregion
    @Override
    public QueryWrapper<AuditInfo> getQueryWrapper(AuditInfoQuery auditInfoQuery){
        QueryWrapper<AuditInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = auditInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = auditInfoQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String fileTypeName = auditInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName) ,"file_type_name",fileTypeName);

        String fileName = auditInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName) ,"file_name",fileName);

        String fileType = auditInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

        String isAgree = auditInfoQuery.getIsAgree();
        queryWrapper.eq(StringUtils.isNotEmpty(isAgree) ,"is_agree",isAgree);

        Date auditTime = auditInfoQuery.getAuditTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginAuditTime"))&&StringUtils.isNotNull(params.get("endAuditTime")),"audit_time",params.get("beginAuditTime"),params.get("endAuditTime"));

        Date createTime = auditInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<AuditInfoVo> convertVoList(List<AuditInfo> auditInfoList) {
        if (StringUtils.isEmpty(auditInfoList)) {
            return Collections.emptyList();
        }
        return auditInfoList.stream().map(AuditInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入审核信息数据
     *
     * @param auditInfoList 审核信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importAuditInfoData(List<AuditInfo> auditInfoList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(auditInfoList) || auditInfoList.size() == 0)
        {
            throw new ServiceException("导入审核信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (AuditInfo auditInfo : auditInfoList)
        {
            try
            {
                // 验证是否存在这个审核信息
                Long id = auditInfo.getId();
                AuditInfo auditInfoExist = null;
                if (StringUtils.isNotNull(id))
                {
                    auditInfoExist = auditInfoMapper.selectAuditInfoById(id);
                }
                if (StringUtils.isNull(auditInfoExist))
                {
                    BeanValidators.validateWithException(validator, auditInfo);
                    auditInfo.setCreateTime(DateUtils.getNowDate());
                    auditInfoMapper.insertAuditInfo(auditInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、审核信息 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, auditInfo);
                    auditInfo.setUpdateTime(DateUtils.getNowDate());
                    auditInfoMapper.updateAuditInfo(auditInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、审核信息 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、审核信息 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = auditInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、审核信息 " + idStr + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
