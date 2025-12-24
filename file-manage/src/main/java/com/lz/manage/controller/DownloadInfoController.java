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
import com.lz.manage.model.domain.DownloadInfo;
import com.lz.manage.model.vo.downloadInfo.DownloadInfoVo;
import com.lz.manage.model.dto.downloadInfo.DownloadInfoQuery;
import com.lz.manage.model.dto.downloadInfo.DownloadInfoInsert;
import com.lz.manage.model.dto.downloadInfo.DownloadInfoEdit;
import com.lz.manage.service.IDownloadInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 下载记录Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/info")
public class DownloadInfoController extends BaseController
{
    @Resource
    private IDownloadInfoService downloadInfoService;

    /**
     * 查询下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(DownloadInfoQuery downloadInfoQuery)
    {
        DownloadInfo downloadInfo = DownloadInfoQuery.queryToObj(downloadInfoQuery);
        startPage();
        List<DownloadInfo> list = downloadInfoService.selectDownloadInfoList(downloadInfo);
        List<DownloadInfoVo> listVo= list.stream().map(DownloadInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出下载记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:info:export')")
    @Log(title = "下载记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DownloadInfoQuery downloadInfoQuery)
    {
        DownloadInfo downloadInfo = DownloadInfoQuery.queryToObj(downloadInfoQuery);
        List<DownloadInfo> list = downloadInfoService.selectDownloadInfoList(downloadInfo);
        ExcelUtil<DownloadInfo> util = new ExcelUtil<DownloadInfo>(DownloadInfo.class);
        util.exportExcel(response, list, "下载记录数据");
    }

    /**
     * 获取下载记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        DownloadInfo downloadInfo = downloadInfoService.selectDownloadInfoById(id);
        return success(DownloadInfoVo.objToVo(downloadInfo));
    }

    /**
     * 新增下载记录
     */
    @PreAuthorize("@ss.hasPermi('manage:info:add')")
    @Log(title = "下载记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DownloadInfoInsert downloadInfoInsert)
    {
        DownloadInfo downloadInfo = DownloadInfoInsert.insertToObj(downloadInfoInsert);
        return toAjax(downloadInfoService.insertDownloadInfo(downloadInfo));
    }

    /**
     * 修改下载记录
     */
    @PreAuthorize("@ss.hasPermi('manage:info:edit')")
    @Log(title = "下载记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DownloadInfoEdit downloadInfoEdit)
    {
        DownloadInfo downloadInfo = DownloadInfoEdit.editToObj(downloadInfoEdit);
        return toAjax(downloadInfoService.updateDownloadInfo(downloadInfo));
    }

    /**
     * 删除下载记录
     */
    @PreAuthorize("@ss.hasPermi('manage:info:remove')")
    @Log(title = "下载记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(downloadInfoService.deleteDownloadInfoByIds(ids));
    }

    /**
     * 导入下载记录数据
     */
    @PreAuthorize("@ss.hasPermi('manage:info:import')")
    @Log(title = "下载记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<DownloadInfo> util = new ExcelUtil<DownloadInfo>(DownloadInfo.class);
        List<DownloadInfo> downloadInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = downloadInfoService.importDownloadInfoData(downloadInfoList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载下载记录导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<DownloadInfo> util = new ExcelUtil<DownloadInfo>(DownloadInfo.class);
        util.importTemplateExcel(response, "下载记录数据");
    }
}
