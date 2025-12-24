package com.lz.manage.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.manage.model.domain.FileType;
import com.lz.manage.model.vo.fileType.FileTypeVo;
import com.lz.manage.model.dto.fileType.FileTypeQuery;
import com.lz.manage.model.dto.fileType.FileTypeInsert;
import com.lz.manage.model.dto.fileType.FileTypeEdit;
import com.lz.manage.service.IFileTypeService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 文件分类Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/fileType")
public class FileTypeController extends BaseController
{
    @Resource
    private IFileTypeService fileTypeService;

    /**
     * 查询文件分类列表
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileTypeQuery fileTypeQuery)
    {
        FileType fileType = FileTypeQuery.queryToObj(fileTypeQuery);
        startPage();
        List<FileType> list = fileTypeService.selectFileTypeList(fileType);
        List<FileTypeVo> listVo= list.stream().map(FileTypeVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出文件分类列表
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:export')")
    @Log(title = "文件分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileTypeQuery fileTypeQuery)
    {
        FileType fileType = FileTypeQuery.queryToObj(fileTypeQuery);
        List<FileType> list = fileTypeService.selectFileTypeList(fileType);
        ExcelUtil<FileType> util = new ExcelUtil<FileType>(FileType.class);
        util.exportExcel(response, list, "文件分类数据");
    }

    /**
     * 获取文件分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        FileType fileType = fileTypeService.selectFileTypeById(id);
        return success(FileTypeVo.objToVo(fileType));
    }

    /**
     * 新增文件分类
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:add')")
    @Log(title = "文件分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FileTypeInsert fileTypeInsert)
    {
        FileType fileType = FileTypeInsert.insertToObj(fileTypeInsert);
        return toAjax(fileTypeService.insertFileType(fileType));
    }

    /**
     * 修改文件分类
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:edit')")
    @Log(title = "文件分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileTypeEdit fileTypeEdit)
    {
        FileType fileType = FileTypeEdit.editToObj(fileTypeEdit);
        return toAjax(fileTypeService.updateFileType(fileType));
    }

    /**
     * 删除文件分类
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:remove')")
    @Log(title = "文件分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fileTypeService.deleteFileTypeByIds(ids));
    }

    /**
     * 导入文件分类数据
     */
    @PreAuthorize("@ss.hasPermi('manage:fileType:import')")
    @Log(title = "文件分类", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<FileType> util = new ExcelUtil<FileType>(FileType.class);
        List<FileType> fileTypeList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = fileTypeService.importFileTypeData(fileTypeList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载文件分类导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<FileType> util = new ExcelUtil<FileType>(FileType.class);
        util.importTemplateExcel(response, "文件分类数据");
    }
}
