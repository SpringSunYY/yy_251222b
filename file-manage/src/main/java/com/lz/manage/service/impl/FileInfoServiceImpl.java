package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.DataScope;
import com.lz.common.config.RuoYiConfig;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.file.FileUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.FileInfoMapper;
import com.lz.manage.model.domain.CollectInfo;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.model.domain.FileType;
import com.lz.manage.model.dto.fileInfo.FileInfoQuery;
import com.lz.manage.model.enums.IsPublicEnum;
import com.lz.manage.model.vo.fileInfo.FileInfoVo;
import com.lz.manage.service.ICollectInfoService;
import com.lz.manage.service.IFileInfoService;
import com.lz.manage.service.IFileTypeService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {
    private static final Logger log = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IFileTypeService fileTypeService;

    @Resource
    @Lazy
    private ICollectInfoService collectInfoService;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码

    /**
     * 查询文件信息
     *
     * @param id 文件信息主键
     * @return 文件信息
     */
    @Override
    public FileInfo selectFileInfoById(Long id) {
        return fileInfoMapper.selectFileInfoById(id);
    }

    /**
     * 查询文件信息列表
     *
     * @param fileInfo 文件信息
     * @return 文件信息
     */
    @DataScope(userAlias = "tb_file_info", deptAlias = "tb_file_info")
    @Override
    public List<FileInfo> selectFileInfoList(FileInfo fileInfo) {
        List<FileInfo> fileInfos = fileInfoMapper.selectFileInfoList(fileInfo);
        for (FileInfo info : fileInfos) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
        }
        return fileInfos;
    }

    /**
     * 新增文件信息
     *
     * @param fileInfo 文件信息
     * @return 结果
     */
    @Override
    public int insertFileInfo(FileInfo fileInfo) {
        //新增默认为私有
        fileInfo.setIsPublic(IsPublicEnum.IS_PUBLIC_0.getValue());
        fileInfo.setUserId(SecurityUtils.getUserId());
        initData(fileInfo);
        fileInfo.setCreateTime(DateUtils.getNowDate());
        return fileInfoMapper.insertFileInfo(fileInfo);
    }

    /**
     * 修改文件信息
     *
     * @param fileInfo 文件信息
     * @return 结果
     */
    @Override
    public int updateFileInfo(FileInfo fileInfo) {
        initData(fileInfo);
        fileInfo.setUpdateTime(DateUtils.getNowDate());
        return fileInfoMapper.updateFileInfo(fileInfo);
    }

    private void initData(FileInfo fileInfo) {
        //查询文件分类是否存在
        FileType fileType = fileTypeService.selectFileTypeById(fileInfo.getFileTypeId());
        if (StringUtils.isNull(fileType)) {
            throw new ServiceException("文件分类不存在！");
        }
        fileInfo.setFileTypeName(fileType.getName());
        //获取文件大小
        String fileUrl = fileInfo.getFileUrl();
        //去掉前缀/profile
        fileUrl = fileUrl.replace("/profile", "");
        fileInfo.setFileSize(FileUtils.getFileSize(RuoYiConfig.getProfile() + fileUrl));
        //获取文件后缀
        fileInfo.setFileType(FileUtils.getSuffixName(fileUrl));
    }

    /**
     * 批量删除文件信息
     *
     * @param ids 需要删除的文件信息主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoByIds(Long[] ids) {
        return fileInfoMapper.deleteFileInfoByIds(ids);
    }

    /**
     * 删除文件信息信息
     *
     * @param id 文件信息主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoById(Long id) {
        return fileInfoMapper.deleteFileInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<FileInfo> getQueryWrapper(FileInfoQuery fileInfoQuery) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = fileInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = fileInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String fileTypeName = fileInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName), "file_type_name", fileTypeName);

        String fileName = fileInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName), "file_name", fileName);

        String fileType = fileInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType), "file_type", fileType);

        String isPublic = fileInfoQuery.getIsPublic();
        queryWrapper.eq(StringUtils.isNotEmpty(isPublic), "is_public", isPublic);

        Date createTime = fileInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<FileInfoVo> convertVoList(List<FileInfo> fileInfoList) {
        if (StringUtils.isEmpty(fileInfoList)) {
            return Collections.emptyList();
        }
        return fileInfoList.stream().map(FileInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入文件信息数据
     *
     * @param fileInfoList    文件信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importFileInfoData(List<FileInfo> fileInfoList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(fileInfoList) || fileInfoList.size() == 0) {
            throw new ServiceException("导入文件信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FileInfo fileInfo : fileInfoList) {
            try {
                // 验证是否存在这个文件信息
                Long id = fileInfo.getId();
                FileInfo fileInfoExist = null;
                if (StringUtils.isNotNull(id)) {
                    fileInfoExist = fileInfoMapper.selectFileInfoById(id);
                }
                if (StringUtils.isNull(fileInfoExist)) {
                    BeanValidators.validateWithException(validator, fileInfo);
                    fileInfo.setCreateTime(DateUtils.getNowDate());
                    fileInfoMapper.insertFileInfo(fileInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、文件信息 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, fileInfo);
                    fileInfo.setUpdateTime(DateUtils.getNowDate());
                    fileInfoMapper.updateFileInfo(fileInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、文件信息 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、文件信息 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = fileInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、文件信息 " + idStr + " 导入失败：";
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

    @Override
    public List<FileInfo> selectFileInfoListByPublic(FileInfo fileInfo) {
        fileInfo.setIsPublic(IsPublicEnum.IS_PUBLIC_1.getValue());
        List<FileInfo> fileInfos = fileInfoMapper.selectFileInfoList(fileInfo);
        Long userId = SecurityUtils.getUserId();
        for (FileInfo info : fileInfos) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
            CollectInfo collect = collectInfoService.isCollect(info.getId(), userId);
            if (StringUtils.isNotNull(collect)) {
                info.setIsCollect(true);
            } else {
                info.setIsCollect(false);
            }
        }
        return fileInfos;
    }

}
