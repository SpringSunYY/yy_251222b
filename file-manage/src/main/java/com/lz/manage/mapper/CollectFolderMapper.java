package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.CollectFolder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 收藏夹Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface CollectFolderMapper extends BaseMapper<CollectFolder>
{
    /**
     * 查询收藏夹
     * 
     * @param id 收藏夹主键
     * @return 收藏夹
     */
    public CollectFolder selectCollectFolderById(Long id);

    /**
     * 查询收藏夹列表
     * 
     * @param collectFolder 收藏夹
     * @return 收藏夹集合
     */
    public List<CollectFolder> selectCollectFolderList(CollectFolder collectFolder);

    /**
     * 新增收藏夹
     * 
     * @param collectFolder 收藏夹
     * @return 结果
     */
    public int insertCollectFolder(CollectFolder collectFolder);

    /**
     * 修改收藏夹
     * 
     * @param collectFolder 收藏夹
     * @return 结果
     */
    public int updateCollectFolder(CollectFolder collectFolder);

    /**
     * 删除收藏夹
     * 
     * @param id 收藏夹主键
     * @return 结果
     */
    public int deleteCollectFolderById(Long id);

    /**
     * 批量删除收藏夹
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCollectFolderByIds(Long[] ids);
}
