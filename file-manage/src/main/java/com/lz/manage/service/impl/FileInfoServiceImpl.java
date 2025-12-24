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
import com.lz.manage.mapper.FileInfoMapper;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.service.IFileInfoService;
import com.lz.manage.model.dto.fileInfo.FileInfoQuery;
import com.lz.manage.model.vo.fileInfo.FileInfoVo;

/**
 * 文件信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService
{
    private static final Logger log = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private FileInfoMapper fileInfoMapper;

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
    public FileInfo selectFileInfoById(Long id)
    {
        return fileInfoMapper.selectFileInfoById(id);
    }

    /**
     * 查询文件信息列表
     *
     * @param fileInfo 文件信息
     * @return 文件信息
     */
    @Override
    public List<FileInfo> selectFileInfoList(FileInfo fileInfo)
    {
        return fileInfoMapper.selectFileInfoList(fileInfo);
    }

    /**
     * 新增文件信息
     *
     * @param fileInfo 文件信息
     * @return 结果
     */
    @Override
    public int insertFileInfo(FileInfo fileInfo)
    {
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
    public int updateFileInfo(FileInfo fileInfo)
    {
        fileInfo.setUpdateTime(DateUtils.getNowDate());
        return fileInfoMapper.updateFileInfo(fileInfo);
    }

    /**
     * 批量删除文件信息
     *
     * @param ids 需要删除的文件信息主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoByIds(Long[] ids)
    {
        return fileInfoMapper.deleteFileInfoByIds(ids);
    }

    /**
     * 删除文件信息信息
     *
     * @param id 文件信息主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoById(Long id)
    {
        return fileInfoMapper.deleteFileInfoById(id);
    }
    //endregion
    @Override
    public QueryWrapper<FileInfo> getQueryWrapper(FileInfoQuery fileInfoQuery){
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = fileInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = fileInfoQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String fileTypeName = fileInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName) ,"file_type_name",fileTypeName);

        String fileName = fileInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName) ,"file_name",fileName);

        String fileType = fileInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

        String isPublic = fileInfoQuery.getIsPublic();
        queryWrapper.eq(StringUtils.isNotEmpty(isPublic) ,"is_public",isPublic);

        Date createTime = fileInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

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
     * @param fileInfoList 文件信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importFileInfoData(List<FileInfo> fileInfoList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(fileInfoList) || fileInfoList.size() == 0)
        {
            throw new ServiceException("导入文件信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FileInfo fileInfo : fileInfoList)
        {
            try
            {
                // 验证是否存在这个文件信息
                Long id = fileInfo.getId();
                FileInfo fileInfoExist = null;
                if (StringUtils.isNotNull(id))
                {
                    fileInfoExist = fileInfoMapper.selectFileInfoById(id);
                }
                if (StringUtils.isNull(fileInfoExist))
                {
                    BeanValidators.validateWithException(validator, fileInfo);
                    fileInfo.setCreateTime(DateUtils.getNowDate());
                    fileInfoMapper.insertFileInfo(fileInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、文件信息 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, fileInfo);
                    fileInfo.setUpdateTime(DateUtils.getNowDate());
                    fileInfoMapper.updateFileInfo(fileInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、文件信息 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、文件信息 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = fileInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、文件信息 " + idStr + " 导入失败：";
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
