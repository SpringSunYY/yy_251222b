package com.lz.manage.controller;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.model.dto.fileInfo.FileInfoEdit;
import com.lz.manage.model.dto.fileInfo.FileInfoInsert;
import com.lz.manage.model.dto.fileInfo.FileInfoQuery;
import com.lz.manage.model.vo.fileInfo.FileInfoPublicVo;
import com.lz.manage.model.vo.fileInfo.FileInfoVo;
import com.lz.manage.service.IFileInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件信息Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/fileInfo")
public class FileInfoController extends BaseController {
    @Resource
    private IFileInfoService fileInfoService;

    /**
     * 查询文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileInfoQuery fileInfoQuery) {
        FileInfo fileInfo = FileInfoQuery.queryToObj(fileInfoQuery);
        startPage();
        List<FileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        List<FileInfoVo> listVo = list.stream().map(FileInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 公开图片
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:list')")
    @GetMapping("/public")
    public TableDataInfo publicList(FileInfoQuery fileInfoQuery) {
        FileInfo fileInfo = FileInfoQuery.queryToObj(fileInfoQuery);
        startPage();
        List<FileInfo> list = fileInfoService.selectFileInfoListByPublic(fileInfo);
        List<FileInfoPublicVo> collect = list.stream().map(FileInfoPublicVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(collect);
        return table;
    }

    /**
     * 导出文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:export')")
    @Log(title = "文件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileInfoQuery fileInfoQuery) {
        FileInfo fileInfo = FileInfoQuery.queryToObj(fileInfoQuery);
        List<FileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        ExcelUtil<FileInfo> util = new ExcelUtil<FileInfo>(FileInfo.class);
        util.exportExcel(response, list, "文件信息数据");
    }

    /**
     * 获取文件信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        FileInfo fileInfo = fileInfoService.selectFileInfoById(id);
        return success(FileInfoVo.objToVo(fileInfo));
    }

    /**
     * 新增文件信息
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:add')")
    @Log(title = "文件信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FileInfoInsert fileInfoInsert) {
        FileInfo fileInfo = FileInfoInsert.insertToObj(fileInfoInsert);
        return toAjax(fileInfoService.insertFileInfo(fileInfo));
    }

    /**
     * 修改文件信息
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:edit')")
    @Log(title = "文件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileInfoEdit fileInfoEdit) {
        FileInfo fileInfo = FileInfoEdit.editToObj(fileInfoEdit);
        return toAjax(fileInfoService.updateFileInfo(fileInfo));
    }

    /**
     * 删除文件信息
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:remove')")
    @Log(title = "文件信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(fileInfoService.deleteFileInfoByIds(ids));
    }

    /**
     * 导入文件信息数据
     */
    @PreAuthorize("@ss.hasPermi('manage:fileInfo:import')")
    @Log(title = "文件信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<FileInfo> util = new ExcelUtil<FileInfo>(FileInfo.class);
        List<FileInfo> fileInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = fileInfoService.importFileInfoData(fileInfoList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载文件信息导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<FileInfo> util = new ExcelUtil<FileInfo>(FileInfo.class);
        util.importTemplateExcel(response, "文件信息数据");
    }
}
