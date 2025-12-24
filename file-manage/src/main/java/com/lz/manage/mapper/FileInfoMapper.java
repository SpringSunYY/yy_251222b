package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 文件信息Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface FileInfoMapper extends BaseMapper<FileInfo>
{
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
     * 删除文件信息
     * 
     * @param id 文件信息主键
     * @return 结果
     */
    public int deleteFileInfoById(Long id);

    /**
     * 批量删除文件信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileInfoByIds(Long[] ids);
}
