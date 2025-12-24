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
import com.lz.manage.mapper.CollectInfoMapper;
import com.lz.manage.model.domain.CollectInfo;
import com.lz.manage.service.ICollectInfoService;
import com.lz.manage.model.dto.collectInfo.CollectInfoQuery;
import com.lz.manage.model.vo.collectInfo.CollectInfoVo;

/**
 * 收藏记录Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class CollectInfoServiceImpl extends ServiceImpl<CollectInfoMapper, CollectInfo> implements ICollectInfoService
{
    private static final Logger log = LoggerFactory.getLogger(CollectInfoServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private CollectInfoMapper collectInfoMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询收藏记录
     *
     * @param id 收藏记录主键
     * @return 收藏记录
     */
    @Override
    public CollectInfo selectCollectInfoById(Long id)
    {
        return collectInfoMapper.selectCollectInfoById(id);
    }

    /**
     * 查询收藏记录列表
     *
     * @param collectInfo 收藏记录
     * @return 收藏记录
     */
    @Override
    public List<CollectInfo> selectCollectInfoList(CollectInfo collectInfo)
    {
        return collectInfoMapper.selectCollectInfoList(collectInfo);
    }

    /**
     * 新增收藏记录
     *
     * @param collectInfo 收藏记录
     * @return 结果
     */
    @Override
    public int insertCollectInfo(CollectInfo collectInfo)
    {
        collectInfo.setCreateTime(DateUtils.getNowDate());
        return collectInfoMapper.insertCollectInfo(collectInfo);
    }

    /**
     * 修改收藏记录
     *
     * @param collectInfo 收藏记录
     * @return 结果
     */
    @Override
    public int updateCollectInfo(CollectInfo collectInfo)
    {
        return collectInfoMapper.updateCollectInfo(collectInfo);
    }

    /**
     * 批量删除收藏记录
     *
     * @param ids 需要删除的收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteCollectInfoByIds(Long[] ids)
    {
        return collectInfoMapper.deleteCollectInfoByIds(ids);
    }

    /**
     * 删除收藏记录信息
     *
     * @param id 收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteCollectInfoById(Long id)
    {
        return collectInfoMapper.deleteCollectInfoById(id);
    }
    //endregion
    @Override
    public QueryWrapper<CollectInfo> getQueryWrapper(CollectInfoQuery collectInfoQuery){
        QueryWrapper<CollectInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = collectInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = collectInfoQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String folderName = collectInfoQuery.getFolderName();
        queryWrapper.like(StringUtils.isNotEmpty(folderName) ,"folder_name",folderName);

        Long fileTypeId = collectInfoQuery.getFileTypeId();
        queryWrapper.eq( StringUtils.isNotNull(fileTypeId),"file_type_id",fileTypeId);

        String fileTypeName = collectInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName) ,"file_type_name",fileTypeName);

        Long fileId = collectInfoQuery.getFileId();
        queryWrapper.eq( StringUtils.isNotNull(fileId),"file_id",fileId);

        String fileName = collectInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName) ,"file_name",fileName);

        String fileType = collectInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

        Date createTime = collectInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<CollectInfoVo> convertVoList(List<CollectInfo> collectInfoList) {
        if (StringUtils.isEmpty(collectInfoList)) {
            return Collections.emptyList();
        }
        return collectInfoList.stream().map(CollectInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入收藏记录数据
     *
     * @param collectInfoList 收藏记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importCollectInfoData(List<CollectInfo> collectInfoList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(collectInfoList) || collectInfoList.size() == 0)
        {
            throw new ServiceException("导入收藏记录数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CollectInfo collectInfo : collectInfoList)
        {
            try
            {
                // 验证是否存在这个收藏记录
                Long id = collectInfo.getId();
                CollectInfo collectInfoExist = null;
                if (StringUtils.isNotNull(id))
                {
                    collectInfoExist = collectInfoMapper.selectCollectInfoById(id);
                }
                if (StringUtils.isNull(collectInfoExist))
                {
                    BeanValidators.validateWithException(validator, collectInfo);
                    collectInfo.setCreateTime(DateUtils.getNowDate());
                    collectInfoMapper.insertCollectInfo(collectInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、收藏记录 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, collectInfo);
                    collectInfoMapper.updateCollectInfo(collectInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、收藏记录 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、收藏记录 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = collectInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、收藏记录 " + idStr + " 导入失败：";
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
