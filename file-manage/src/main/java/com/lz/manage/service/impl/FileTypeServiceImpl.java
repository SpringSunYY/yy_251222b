package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.FileTypeMapper;
import com.lz.manage.model.domain.FileType;
import com.lz.manage.model.dto.fileType.FileTypeQuery;
import com.lz.manage.model.vo.fileType.FileTypeVo;
import com.lz.manage.service.IFileTypeService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件分类Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class FileTypeServiceImpl extends ServiceImpl<FileTypeMapper, FileType> implements IFileTypeService {
    private static final Logger log = LoggerFactory.getLogger(FileTypeServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private FileTypeMapper fileTypeMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码

    /**
     * 查询文件分类
     *
     * @param id 文件分类主键
     * @return 文件分类
     */
    @Override
    public FileType selectFileTypeById(Long id) {
        return fileTypeMapper.selectFileTypeById(id);
    }

    /**
     * 查询文件分类列表
     *
     * @param fileType 文件分类
     * @return 文件分类
     */
    @Override
    public List<FileType> selectFileTypeList(FileType fileType) {
        List<FileType> fileTypes = fileTypeMapper.selectFileTypeList(fileType);
        for (FileType info : fileTypes) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
        }
        return fileTypes;
    }

    /**
     * 新增文件分类
     *
     * @param fileType 文件分类
     * @return 结果
     */
    @Override
    public int insertFileType(FileType fileType) {
        fileType.setCreateTime(DateUtils.getNowDate());
        fileType.setUserId(SecurityUtils.getUserId());
        return fileTypeMapper.insertFileType(fileType);
    }

    /**
     * 修改文件分类
     *
     * @param fileType 文件分类
     * @return 结果
     */
    @Override
    public int updateFileType(FileType fileType) {
        fileType.setUpdateTime(DateUtils.getNowDate());
        return fileTypeMapper.updateFileType(fileType);
    }

    /**
     * 批量删除文件分类
     *
     * @param ids 需要删除的文件分类主键
     * @return 结果
     */
    @Override
    public int deleteFileTypeByIds(Long[] ids) {
        return fileTypeMapper.deleteFileTypeByIds(ids);
    }

    /**
     * 删除文件分类信息
     *
     * @param id 文件分类主键
     * @return 结果
     */
    @Override
    public int deleteFileTypeById(Long id) {
        return fileTypeMapper.deleteFileTypeById(id);
    }

    //endregion
    @Override
    public QueryWrapper<FileType> getQueryWrapper(FileTypeQuery fileTypeQuery) {
        QueryWrapper<FileType> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = fileTypeQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = fileTypeQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String name = fileTypeQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        Date createTime = fileTypeQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<FileTypeVo> convertVoList(List<FileType> fileTypeList) {
        if (StringUtils.isEmpty(fileTypeList)) {
            return Collections.emptyList();
        }
        return fileTypeList.stream().map(FileTypeVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入文件分类数据
     *
     * @param fileTypeList    文件分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importFileTypeData(List<FileType> fileTypeList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(fileTypeList) || fileTypeList.size() == 0) {
            throw new ServiceException("导入文件分类数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FileType fileType : fileTypeList) {
            try {
                // 验证是否存在这个文件分类
                Long id = fileType.getId();
                FileType fileTypeExist = null;
                if (StringUtils.isNotNull(id)) {
                    fileTypeExist = fileTypeMapper.selectFileTypeById(id);
                }
                if (StringUtils.isNull(fileTypeExist)) {
                    BeanValidators.validateWithException(validator, fileType);
                    fileType.setCreateTime(DateUtils.getNowDate());
                    fileTypeMapper.insertFileType(fileType);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、文件分类 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, fileType);
                    fileType.setUpdateTime(DateUtils.getNowDate());
                    fileTypeMapper.updateFileType(fileType);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、文件分类 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、文件分类 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = fileType.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、文件分类 " + idStr + " 导入失败：";
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
