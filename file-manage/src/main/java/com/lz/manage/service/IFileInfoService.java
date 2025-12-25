package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.model.vo.fileInfo.FileInfoVo;
import com.lz.manage.model.dto.fileInfo.FileInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 文件信息Service接口
 *
 * @author YY
 * @date 2025-12-24
 */
public interface IFileInfoService extends IService<FileInfo>
{
    //region mybatis代码
    /**
     * 查询文件信息
     *
     * @param id 文件信息主键
     * @return 文件信息
     */
    public FileInfo selectFileInfoById(Long id);

    /**
     * 查询文件信息列表
     *
     * @param fileInfo 文件信息
     * @return 文件信息集合
     */
    public List<FileInfo> selectFileInfoList(FileInfo fileInfo);

    /**
     * 新增文件信息
     *
     * @param fileInfo 文件信息
     * @return 结果
     */
    public int insertFileInfo(FileInfo fileInfo);

    /**
     * 修改文件信息
     *
     * @param fileInfo 文件信息
     * @return 结果
     */
    public int updateFileInfo(FileInfo fileInfo);

    /**
     * 批量删除文件信息
     *
     * @param ids 需要删除的文件信息主键集合
     * @return 结果
     */
    public int deleteFileInfoByIds(Long[] ids);

    /**
     * 删除文件信息信息
     *
     * @param id 文件信息主键
     * @return 结果
     */
    public int deleteFileInfoById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param fileInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<FileInfo> getQueryWrapper(FileInfoQuery fileInfoQuery);

    /**
     * 转换vo
     *
     * @param fileInfoList FileInfo集合
     * @return FileInfoVO集合
     */
    List<FileInfoVo> convertVoList(List<FileInfo> fileInfoList);

    /**
     * 导入文件信息数据
     *
     * @param fileInfoList 文件信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFileInfoData(List<FileInfo> fileInfoList, Boolean isUpdateSupport, String operName);

    /**
     * 查看公开图片
     * @param fileInfo
     * @return
     */
    List<FileInfo> selectFileInfoListByPublic(FileInfo fileInfo);
}
