package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.DataScope;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.AuditInfoMapper;
import com.lz.manage.model.domain.AuditInfo;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.model.dto.auditInfo.AuditInfoQuery;
import com.lz.manage.model.enums.IsAgreeEnum;
import com.lz.manage.model.enums.IsPublicEnum;
import com.lz.manage.model.vo.auditInfo.AuditInfoVo;
import com.lz.manage.service.IAuditInfoService;
import com.lz.manage.service.IFileInfoService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审核信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class AuditInfoServiceImpl extends ServiceImpl<AuditInfoMapper, AuditInfo> implements IAuditInfoService {
    private static final Logger log = LoggerFactory.getLogger(AuditInfoServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private AuditInfoMapper auditInfoMapper;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IFileInfoService fileInfoService;

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
    public AuditInfo selectAuditInfoById(Long id) {
        return auditInfoMapper.selectAuditInfoById(id);
    }

    /**
     * 查询审核信息列表
     *
     * @param auditInfo 审核信息
     * @return 审核信息
     */
    @DataScope(deptAlias = "tb_audit_info", userAlias = "tb_audit_info")
    @Override
    public List<AuditInfo> selectAuditInfoList(AuditInfo auditInfo) {
        List<AuditInfo> auditInfos = auditInfoMapper.selectAuditInfoList(auditInfo);
        for (AuditInfo info : auditInfos) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
            if (StringUtils.isNotNull(info.getAuditUserId())) {
                SysUser auditUser = sysUserService.selectUserById(info.getAuditUserId());
                if (StringUtils.isNotNull(auditUser)) {
                    info.setAuditUserName(auditUser.getUserName());
                }
            }
        }
        return auditInfos;
    }

    /**
     * 新增审核信息
     *
     * @param auditInfo 审核信息
     * @return 结果
     */
    @Override
    public int insertAuditInfo(AuditInfo auditInfo) {
        auditInfo.setIsAgree(IsAgreeEnum.IS_AGREE_0.getValue());
        initData(auditInfo);
        auditInfo.setUserId(SecurityUtils.getUserId());
        auditInfo.setCreateTime(DateUtils.getNowDate());
        return auditInfoMapper.insertAuditInfo(auditInfo);
    }

    private FileInfo initData(AuditInfo auditInfo) {
        //首先查询文件是否存在
        FileInfo fileInfo = fileInfoService.selectFileInfoById(auditInfo.getFileId());
        if (StringUtils.isNull(fileInfo)) {
            throw new ServiceException("文件不存在");
        }
        BeanUtils.copyProperties(fileInfo, auditInfo);
        //如果文件已经公开无需申请
        if (IsPublicEnum.IS_PUBLIC_1.getValue().equals(fileInfo.getIsPublic())) {
            //删除所有该文件待审核的申请
            this.remove(new LambdaQueryWrapper<>(new AuditInfo())
                    .eq(AuditInfo::getFileId, auditInfo.getFileId())
                    .eq(AuditInfo::getIsAgree, IsAgreeEnum.IS_AGREE_0.getValue()));
            throw new ServiceException("文件已经公开无需申请,请刷新页面查看状态");
        }
        return fileInfo;
    }

    /**
     * 修改审核信息
     *
     * @param auditInfo 审核信息
     * @return 结果
     */
    @Override
    public int updateAuditInfo(AuditInfo auditInfo) {
        FileInfo fileInfo = initData(auditInfo);
        auditInfo.setUpdateTime(DateUtils.getNowDate());
        //查询数据库内容，如果是已经审核通过不可修改
        AuditInfo auditInfoDb = auditInfoMapper.selectAuditInfoById(auditInfo.getId());
        if (IsAgreeEnum.IS_AGREE_1.getValue().equals(auditInfoDb.getIsAgree())) {
            throw new ServiceException("文件已经审核通过，不可修改");
        }
        //如果传过来的是公开
        if (IsPublicEnum.IS_PUBLIC_1.getValue().equals(auditInfo.getIsAgree())) {
            fileInfo.setIsPublic(IsPublicEnum.IS_PUBLIC_1.getValue());
            fileInfoService.updateFileInfo(fileInfo);
        }
        //如果审核状态不等于数据库内容
        if (!auditInfo.getIsAgree().equals(auditInfoDb.getIsAgree())) {
            auditInfo.setAuditUserId(SecurityUtils.getUserId());
            auditInfo.setAuditTime(DateUtils.getNowDate());
        }
        return auditInfoMapper.updateAuditInfo(auditInfo);
    }

    /**
     * 批量删除审核信息
     *
     * @param ids 需要删除的审核信息主键
     * @return 结果
     */
    @Override
    public int deleteAuditInfoByIds(Long[] ids) {
        return auditInfoMapper.deleteAuditInfoByIds(ids);
    }

    /**
     * 删除审核信息信息
     *
     * @param id 审核信息主键
     * @return 结果
     */
    @Override
    public int deleteAuditInfoById(Long id) {
        return auditInfoMapper.deleteAuditInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<AuditInfo> getQueryWrapper(AuditInfoQuery auditInfoQuery) {
        QueryWrapper<AuditInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = auditInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = auditInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String fileTypeName = auditInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName), "file_type_name", fileTypeName);

        String fileName = auditInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName), "file_name", fileName);

        String fileType = auditInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType), "file_type", fileType);

        String isAgree = auditInfoQuery.getIsAgree();
        queryWrapper.eq(StringUtils.isNotEmpty(isAgree), "is_agree", isAgree);

        Date auditTime = auditInfoQuery.getAuditTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginAuditTime")) && StringUtils.isNotNull(params.get("endAuditTime")), "audit_time", params.get("beginAuditTime"), params.get("endAuditTime"));

        Date createTime = auditInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

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
     * @param auditInfoList   审核信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAuditInfoData(List<AuditInfo> auditInfoList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(auditInfoList) || auditInfoList.size() == 0) {
            throw new ServiceException("导入审核信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (AuditInfo auditInfo : auditInfoList) {
            try {
                // 验证是否存在这个审核信息
                Long id = auditInfo.getId();
                AuditInfo auditInfoExist = null;
                if (StringUtils.isNotNull(id)) {
                    auditInfoExist = auditInfoMapper.selectAuditInfoById(id);
                }
                if (StringUtils.isNull(auditInfoExist)) {
                    BeanValidators.validateWithException(validator, auditInfo);
                    auditInfo.setCreateTime(DateUtils.getNowDate());
                    auditInfoMapper.insertAuditInfo(auditInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、审核信息 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, auditInfo);
                    auditInfo.setUpdateTime(DateUtils.getNowDate());
                    auditInfoMapper.updateAuditInfo(auditInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、审核信息 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、审核信息 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = auditInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、审核信息 " + idStr + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
