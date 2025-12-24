package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.CollectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 收藏记录Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface CollectInfoMapper extends BaseMapper<CollectInfo>
{
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
     * 删除收藏记录
     * 
     * @param id 收藏记录主键
     * @return 结果
     */
    public int deleteCollectInfoById(Long id);

    /**
     * 批量删除收藏记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCollectInfoByIds(Long[] ids);
}
