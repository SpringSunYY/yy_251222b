package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.CollectFolder;
import com.lz.manage.model.vo.collectFolder.CollectFolderVo;
import com.lz.manage.model.dto.collectFolder.CollectFolderQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 收藏夹Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface ICollectFolderService extends IService<CollectFolder>
{
    //region mybatis代码
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
     * 批量删除收藏夹
     * 
     * @param ids 需要删除的收藏夹主键集合
     * @return 结果
     */
    public int deleteCollectFolderByIds(Long[] ids);

    /**
     * 删除收藏夹信息
     * 
     * @param id 收藏夹主键
     * @return 结果
     */
    public int deleteCollectFolderById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param collectFolderQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<CollectFolder> getQueryWrapper(CollectFolderQuery collectFolderQuery);

    /**
     * 转换vo
     *
     * @param collectFolderList CollectFolder集合
     * @return CollectFolderVO集合
     */
    List<CollectFolderVo> convertVoList(List<CollectFolder> collectFolderList);

    /**
     * 导入收藏夹数据
     *
     * @param collectFolderList 收藏夹数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importCollectFolderData(List<CollectFolder> collectFolderList, Boolean isUpdateSupport, String operName);
}
