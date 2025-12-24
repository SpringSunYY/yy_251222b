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
import com.lz.manage.model.domain.CollectFolder;
import com.lz.manage.model.vo.collectFolder.CollectFolderVo;
import com.lz.manage.model.dto.collectFolder.CollectFolderQuery;
import com.lz.manage.model.dto.collectFolder.CollectFolderInsert;
import com.lz.manage.model.dto.collectFolder.CollectFolderEdit;
import com.lz.manage.service.ICollectFolderService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 收藏夹Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/collectFolder")
public class CollectFolderController extends BaseController
{
    @Resource
    private ICollectFolderService collectFolderService;

    /**
     * 查询收藏夹列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:list')")
    @GetMapping("/list")
    public TableDataInfo list(CollectFolderQuery collectFolderQuery)
    {
        CollectFolder collectFolder = CollectFolderQuery.queryToObj(collectFolderQuery);
        startPage();
        List<CollectFolder> list = collectFolderService.selectCollectFolderList(collectFolder);
        List<CollectFolderVo> listVo= list.stream().map(CollectFolderVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出收藏夹列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:export')")
    @Log(title = "收藏夹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CollectFolderQuery collectFolderQuery)
    {
        CollectFolder collectFolder = CollectFolderQuery.queryToObj(collectFolderQuery);
        List<CollectFolder> list = collectFolderService.selectCollectFolderList(collectFolder);
        ExcelUtil<CollectFolder> util = new ExcelUtil<CollectFolder>(CollectFolder.class);
        util.exportExcel(response, list, "收藏夹数据");
    }

    /**
     * 获取收藏夹详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        CollectFolder collectFolder = collectFolderService.selectCollectFolderById(id);
        return success(CollectFolderVo.objToVo(collectFolder));
    }

    /**
     * 新增收藏夹
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:add')")
    @Log(title = "收藏夹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CollectFolderInsert collectFolderInsert)
    {
        CollectFolder collectFolder = CollectFolderInsert.insertToObj(collectFolderInsert);
        return toAjax(collectFolderService.insertCollectFolder(collectFolder));
    }

    /**
     * 修改收藏夹
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:edit')")
    @Log(title = "收藏夹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CollectFolderEdit collectFolderEdit)
    {
        CollectFolder collectFolder = CollectFolderEdit.editToObj(collectFolderEdit);
        return toAjax(collectFolderService.updateCollectFolder(collectFolder));
    }

    /**
     * 删除收藏夹
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:remove')")
    @Log(title = "收藏夹", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(collectFolderService.deleteCollectFolderByIds(ids));
    }

    /**
     * 导入收藏夹数据
     */
    @PreAuthorize("@ss.hasPermi('manage:collectFolder:import')")
    @Log(title = "收藏夹", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CollectFolder> util = new ExcelUtil<CollectFolder>(CollectFolder.class);
        List<CollectFolder> collectFolderList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = collectFolderService.importCollectFolderData(collectFolderList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载收藏夹导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<CollectFolder> util = new ExcelUtil<CollectFolder>(CollectFolder.class);
        util.importTemplateExcel(response, "收藏夹数据");
    }
}
