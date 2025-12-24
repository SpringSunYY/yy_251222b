package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.FileType;
import com.lz.manage.model.vo.fileType.FileTypeVo;
import com.lz.manage.model.dto.fileType.FileTypeQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 文件分类Service接口
 * 
 * @author YY
 * @date 2025-12-24
 */
public interface IFileTypeService extends IService<FileType>
{
    //region mybatis代码
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
     * 批量删除文件分类
     * 
     * @param ids 需要删除的文件分类主键集合
     * @return 结果
     */
    public int deleteFileTypeByIds(Long[] ids);

    /**
     * 删除文件分类信息
     * 
     * @param id 文件分类主键
     * @return 结果
     */
    public int deleteFileTypeById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param fileTypeQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<FileType> getQueryWrapper(FileTypeQuery fileTypeQuery);

    /**
     * 转换vo
     *
     * @param fileTypeList FileType集合
     * @return FileTypeVO集合
     */
    List<FileTypeVo> convertVoList(List<FileType> fileTypeList);

    /**
     * 导入文件分类数据
     *
     * @param fileTypeList 文件分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFileTypeData(List<FileType> fileTypeList, Boolean isUpdateSupport, String operName);
}
