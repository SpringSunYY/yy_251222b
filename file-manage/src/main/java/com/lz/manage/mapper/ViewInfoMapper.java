package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.ViewInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 浏览记录Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface ViewInfoMapper extends BaseMapper<ViewInfo>
{
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
     * 删除浏览记录
     * 
     * @param id 浏览记录主键
     * @return 结果
     */
    public int deleteViewInfoById(Long id);

    /**
     * 批量删除浏览记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteViewInfoByIds(Long[] ids);
}
