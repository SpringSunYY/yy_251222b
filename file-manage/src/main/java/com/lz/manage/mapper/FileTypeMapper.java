package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.FileType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 文件分类Mapper接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface FileTypeMapper extends BaseMapper<FileType>
{
    /**
     * 查询文件分类
     * 
     * @param id 文件分类主键
     * @return 文件分类
     */
    public FileType selectFileTypeById(Long id);

    /**
     * 查询文件分类列表
     * 
     * @param fileType 文件分类
     * @return 文件分类集合
     */
    public List<FileType> selectFileTypeList(FileType fileType);

    /**
     * 新增文件分类
     * 
     * @param fileType 文件分类
     * @return 结果
     */
    public int insertFileType(FileType fileType);

    /**
     * 修改文件分类
     * 
     * @param fileType 文件分类
     * @return 结果
     */
    public int updateFileType(FileType fileType);

    /**
     * 删除文件分类
     * 
     * @param id 文件分类主键
     * @return 结果
     */
    public int deleteFileTypeById(Long id);

    /**
     * 批量删除文件分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileTypeByIds(Long[] ids);
}
