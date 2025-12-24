package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.CollectInfo;
import com.lz.manage.model.vo.collectInfo.CollectInfoVo;
import com.lz.manage.model.dto.collectInfo.CollectInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 收藏记录Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface ICollectInfoService extends IService<CollectInfo>
{
    //region mybatis代码
    /**
     * 查询收藏记录
     * 
     * @param id 收藏记录主键
     * @return 收藏记录
     */
    public CollectInfo selectCollectInfoById(Long id);

    /**
     * 查询收藏记录列表
     * 
     * @param collectInfo 收藏记录
     * @return 收藏记录集合
     */
    public List<CollectInfo> selectCollectInfoList(CollectInfo collectInfo);

    /**
     * 新增收藏记录
     * 
     * @param collectInfo 收藏记录
     * @return 结果
     */
    public int insertCollectInfo(CollectInfo collectInfo);

    /**
     * 修改收藏记录
     * 
     * @param collectInfo 收藏记录
     * @return 结果
     */
    public int updateCollectInfo(CollectInfo collectInfo);

    /**
     * 批量删除收藏记录
     * 
     * @param ids 需要删除的收藏记录主键集合
     * @return 结果
     */
    public int deleteCollectInfoByIds(Long[] ids);

    /**
     * 删除收藏记录信息
     * 
     * @param id 收藏记录主键
     * @return 结果
     */
    public int deleteCollectInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param collectInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<CollectInfo> getQueryWrapper(CollectInfoQuery collectInfoQuery);

    /**
     * 转换vo
     *
     * @param collectInfoList CollectInfo集合
     * @return CollectInfoVO集合
     */
    List<CollectInfoVo> convertVoList(List<CollectInfo> collectInfoList);

    /**
     * 导入收藏记录数据
     *
     * @param collectInfoList 收藏记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importCollectInfoData(List<CollectInfo> collectInfoList, Boolean isUpdateSupport, String operName);
}
