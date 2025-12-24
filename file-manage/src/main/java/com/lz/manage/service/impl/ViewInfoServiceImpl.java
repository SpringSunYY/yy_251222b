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
import com.lz.manage.mapper.ViewInfoMapper;
import com.lz.manage.model.domain.ViewInfo;
import com.lz.manage.service.IViewInfoService;
import com.lz.manage.model.dto.viewInfo.ViewInfoQuery;
import com.lz.manage.model.vo.viewInfo.ViewInfoVo;

/**
 * 浏览记录Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class ViewInfoServiceImpl extends ServiceImpl<ViewInfoMapper, ViewInfo> implements IViewInfoService
{
    private static final Logger log = LoggerFactory.getLogger(ViewInfoServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private ViewInfoMapper viewInfoMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询浏览记录
     *
     * @param id 浏览记录主键
     * @return 浏览记录
     */
    @Override
    public ViewInfo selectViewInfoById(Long id)
    {
        return viewInfoMapper.selectViewInfoById(id);
    }

    /**
     * 查询浏览记录列表
     *
     * @param viewInfo 浏览记录
     * @return 浏览记录
     */
    @Override
    public List<ViewInfo> selectViewInfoList(ViewInfo viewInfo)
    {
        return viewInfoMapper.selectViewInfoList(viewInfo);
    }

    /**
     * 新增浏览记录
     *
     * @param viewInfo 浏览记录
     * @return 结果
     */
    @Override
    public int insertViewInfo(ViewInfo viewInfo)
    {
        viewInfo.setCreateTime(DateUtils.getNowDate());
        return viewInfoMapper.insertViewInfo(viewInfo);
    }

    /**
     * 修改浏览记录
     *
     * @param viewInfo 浏览记录
     * @return 结果
     */
    @Override
    public int updateViewInfo(ViewInfo viewInfo)
    {
        return viewInfoMapper.updateViewInfo(viewInfo);
    }

    /**
     * 批量删除浏览记录
     *
     * @param ids 需要删除的浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteViewInfoByIds(Long[] ids)
    {
        return viewInfoMapper.deleteViewInfoByIds(ids);
    }

    /**
     * 删除浏览记录信息
     *
     * @param id 浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteViewInfoById(Long id)
    {
        return viewInfoMapper.deleteViewInfoById(id);
    }
    //endregion
    @Override
    public QueryWrapper<ViewInfo> getQueryWrapper(ViewInfoQuery viewInfoQuery){
        QueryWrapper<ViewInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = viewInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = viewInfoQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String fileTypeName = viewInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName) ,"file_type_name",fileTypeName);

        String fileName = viewInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName) ,"file_name",fileName);

        String fileType = viewInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

        Date createTime = viewInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<ViewInfoVo> convertVoList(List<ViewInfo> viewInfoList) {
        if (StringUtils.isEmpty(viewInfoList)) {
            return Collections.emptyList();
        }
        return viewInfoList.stream().map(ViewInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入浏览记录数据
     *
     * @param viewInfoList 浏览记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importViewInfoData(List<ViewInfo> viewInfoList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(viewInfoList) || viewInfoList.size() == 0)
        {
            throw new ServiceException("导入浏览记录数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ViewInfo viewInfo : viewInfoList)
        {
            try
            {
                // 验证是否存在这个浏览记录
                Long id = viewInfo.getId();
                ViewInfo viewInfoExist = null;
                if (StringUtils.isNotNull(id))
                {
                    viewInfoExist = viewInfoMapper.selectViewInfoById(id);
                }
                if (StringUtils.isNull(viewInfoExist))
                {
                    BeanValidators.validateWithException(validator, viewInfo);
                    viewInfo.setCreateTime(DateUtils.getNowDate());
                    viewInfoMapper.insertViewInfo(viewInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、浏览记录 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, viewInfo);
                    viewInfoMapper.updateViewInfo(viewInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、浏览记录 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、浏览记录 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = viewInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、浏览记录 " + idStr + " 导入失败：";
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
