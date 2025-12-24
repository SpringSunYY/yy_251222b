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
import com.lz.manage.mapper.DownloadInfoMapper;
import com.lz.manage.model.domain.DownloadInfo;
import com.lz.manage.service.IDownloadInfoService;
import com.lz.manage.model.dto.downloadInfo.DownloadInfoQuery;
import com.lz.manage.model.vo.downloadInfo.DownloadInfoVo;

/**
 * 下载记录Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class DownloadInfoServiceImpl extends ServiceImpl<DownloadInfoMapper, DownloadInfo> implements IDownloadInfoService
{
    private static final Logger log = LoggerFactory.getLogger(DownloadInfoServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private DownloadInfoMapper downloadInfoMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询下载记录
     *
     * @param id 下载记录主键
     * @return 下载记录
     */
    @Override
    public DownloadInfo selectDownloadInfoById(Long id)
    {
        return downloadInfoMapper.selectDownloadInfoById(id);
    }

    /**
     * 查询下载记录列表
     *
     * @param downloadInfo 下载记录
     * @return 下载记录
     */
    @Override
    public List<DownloadInfo> selectDownloadInfoList(DownloadInfo downloadInfo)
    {
        return downloadInfoMapper.selectDownloadInfoList(downloadInfo);
    }

    /**
     * 新增下载记录
     *
     * @param downloadInfo 下载记录
     * @return 结果
     */
    @Override
    public int insertDownloadInfo(DownloadInfo downloadInfo)
    {
        downloadInfo.setCreateTime(DateUtils.getNowDate());
        return downloadInfoMapper.insertDownloadInfo(downloadInfo);
    }

    /**
     * 修改下载记录
     *
     * @param downloadInfo 下载记录
     * @return 结果
     */
    @Override
    public int updateDownloadInfo(DownloadInfo downloadInfo)
    {
        return downloadInfoMapper.updateDownloadInfo(downloadInfo);
    }

    /**
     * 批量删除下载记录
     *
     * @param ids 需要删除的下载记录主键
     * @return 结果
     */
    @Override
    public int deleteDownloadInfoByIds(Long[] ids)
    {
        return downloadInfoMapper.deleteDownloadInfoByIds(ids);
    }

    /**
     * 删除下载记录信息
     *
     * @param id 下载记录主键
     * @return 结果
     */
    @Override
    public int deleteDownloadInfoById(Long id)
    {
        return downloadInfoMapper.deleteDownloadInfoById(id);
    }
    //endregion
    @Override
    public QueryWrapper<DownloadInfo> getQueryWrapper(DownloadInfoQuery downloadInfoQuery){
        QueryWrapper<DownloadInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = downloadInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = downloadInfoQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String fileTypeName = downloadInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName) ,"file_type_name",fileTypeName);

        String fileName = downloadInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName) ,"file_name",fileName);

        String fileType = downloadInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

        Date createTime = downloadInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<DownloadInfoVo> convertVoList(List<DownloadInfo> downloadInfoList) {
        if (StringUtils.isEmpty(downloadInfoList)) {
            return Collections.emptyList();
        }
        return downloadInfoList.stream().map(DownloadInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入下载记录数据
     *
     * @param downloadInfoList 下载记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDownloadInfoData(List<DownloadInfo> downloadInfoList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(downloadInfoList) || downloadInfoList.size() == 0)
        {
            throw new ServiceException("导入下载记录数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (DownloadInfo downloadInfo : downloadInfoList)
        {
            try
            {
                // 验证是否存在这个下载记录
                Long id = downloadInfo.getId();
                DownloadInfo downloadInfoExist = null;
                if (StringUtils.isNotNull(id))
                {
                    downloadInfoExist = downloadInfoMapper.selectDownloadInfoById(id);
                }
                if (StringUtils.isNull(downloadInfoExist))
                {
                    BeanValidators.validateWithException(validator, downloadInfo);
                    downloadInfo.setCreateTime(DateUtils.getNowDate());
                    downloadInfoMapper.insertDownloadInfo(downloadInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、下载记录 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, downloadInfo);
                    downloadInfoMapper.updateDownloadInfo(downloadInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、下载记录 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、下载记录 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = downloadInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、下载记录 " + idStr + " 导入失败：";
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
