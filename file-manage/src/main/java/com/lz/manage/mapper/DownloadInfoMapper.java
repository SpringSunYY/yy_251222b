package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.DownloadInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 下载记录Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface DownloadInfoMapper extends BaseMapper<DownloadInfo>
{
    /**
     * 查询下载记录
     * 
     * @param id 下载记录主键
     * @return 下载记录
     */
    public DownloadInfo selectDownloadInfoById(Long id);

    /**
     * 查询下载记录列表
     * 
     * @param downloadInfo 下载记录
     * @return 下载记录集合
     */
    public List<DownloadInfo> selectDownloadInfoList(DownloadInfo downloadInfo);

    /**
     * 新增下载记录
     * 
     * @param downloadInfo 下载记录
     * @return 结果
     */
    public int insertDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 修改下载记录
     * 
     * @param downloadInfo 下载记录
     * @return 结果
     */
    public int updateDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 删除下载记录
     * 
     * @param id 下载记录主键
     * @return 结果
     */
    public int deleteDownloadInfoById(Long id);

    /**
     * 批量删除下载记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDownloadInfoByIds(Long[] ids);
}
