package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.ViewInfo;
import com.lz.manage.model.vo.viewInfo.ViewInfoVo;
import com.lz.manage.model.dto.viewInfo.ViewInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 浏览记录Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface IViewInfoService extends IService<ViewInfo>
{
    //region mybatis代码
    /**
     * 查询浏览记录
     * 
     * @param id 浏览记录主键
     * @return 浏览记录
     */
    public ViewInfo selectViewInfoById(Long id);

    /**
     * 查询浏览记录列表
     * 
     * @param viewInfo 浏览记录
     * @return 浏览记录集合
     */
    public List<ViewInfo> selectViewInfoList(ViewInfo viewInfo);

    /**
     * 新增浏览记录
     * 
     * @param viewInfo 浏览记录
     * @return 结果
     */
    public int insertViewInfo(ViewInfo viewInfo);

    /**
     * 修改浏览记录
     * 
     * @param viewInfo 浏览记录
     * @return 结果
     */
    public int updateViewInfo(ViewInfo viewInfo);

    /**
     * 批量删除浏览记录
     * 
     * @param ids 需要删除的浏览记录主键集合
     * @return 结果
     */
    public int deleteViewInfoByIds(Long[] ids);

    /**
     * 删除浏览记录信息
     * 
     * @param id 浏览记录主键
     * @return 结果
     */
    public int deleteViewInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param viewInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ViewInfo> getQueryWrapper(ViewInfoQuery viewInfoQuery);

    /**
     * 转换vo
     *
     * @param viewInfoList ViewInfo集合
     * @return ViewInfoVO集合
     */
    List<ViewInfoVo> convertVoList(List<ViewInfo> viewInfoList);

    /**
     * 导入浏览记录数据
     *
     * @param viewInfoList 浏览记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importViewInfoData(List<ViewInfo> viewInfoList, Boolean isUpdateSupport, String operName);
}
