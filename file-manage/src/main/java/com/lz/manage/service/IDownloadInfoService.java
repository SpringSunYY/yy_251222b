package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.DownloadInfo;
import com.lz.manage.model.vo.downloadInfo.DownloadInfoVo;
import com.lz.manage.model.dto.downloadInfo.DownloadInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 下载记录Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface IDownloadInfoService extends IService<DownloadInfo>
{
    //region mybatis代码
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
     * 批量删除下载记录
     * 
     * @param ids 需要删除的下载记录主键集合
     * @return 结果
     */
    public int deleteDownloadInfoByIds(Long[] ids);

    /**
     * 删除下载记录信息
     * 
     * @param id 下载记录主键
     * @return 结果
     */
    public int deleteDownloadInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param downloadInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<DownloadInfo> getQueryWrapper(DownloadInfoQuery downloadInfoQuery);

    /**
     * 转换vo
     *
     * @param downloadInfoList DownloadInfo集合
     * @return DownloadInfoVO集合
     */
    List<DownloadInfoVo> convertVoList(List<DownloadInfo> downloadInfoList);

    /**
     * 导入下载记录数据
     *
     * @param downloadInfoList 下载记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importDownloadInfoData(List<DownloadInfo> downloadInfoList, Boolean isUpdateSupport, String operName);
}
