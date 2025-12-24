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
import com.lz.manage.model.domain.ViewInfo;
import com.lz.manage.model.vo.viewInfo.ViewInfoVo;
import com.lz.manage.model.dto.viewInfo.ViewInfoQuery;
import com.lz.manage.model.dto.viewInfo.ViewInfoInsert;
import com.lz.manage.model.dto.viewInfo.ViewInfoEdit;
import com.lz.manage.service.IViewInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 浏览记录Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/viewInfo")
public class ViewInfoController extends BaseController
{
    @Resource
    private IViewInfoService viewInfoService;

    /**
     * 查询浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ViewInfoQuery viewInfoQuery)
    {
        ViewInfo viewInfo = ViewInfoQuery.queryToObj(viewInfoQuery);
        startPage();
        List<ViewInfo> list = viewInfoService.selectViewInfoList(viewInfo);
        List<ViewInfoVo> listVo= list.stream().map(ViewInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:export')")
    @Log(title = "浏览记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ViewInfoQuery viewInfoQuery)
    {
        ViewInfo viewInfo = ViewInfoQuery.queryToObj(viewInfoQuery);
        List<ViewInfo> list = viewInfoService.selectViewInfoList(viewInfo);
        ExcelUtil<ViewInfo> util = new ExcelUtil<ViewInfo>(ViewInfo.class);
        util.exportExcel(response, list, "浏览记录数据");
    }

    /**
     * 获取浏览记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ViewInfo viewInfo = viewInfoService.selectViewInfoById(id);
        return success(ViewInfoVo.objToVo(viewInfo));
    }

    /**
     * 新增浏览记录
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:add')")
    @Log(title = "浏览记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ViewInfoInsert viewInfoInsert)
    {
        ViewInfo viewInfo = ViewInfoInsert.insertToObj(viewInfoInsert);
        return toAjax(viewInfoService.insertViewInfo(viewInfo));
    }

    /**
     * 修改浏览记录
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:edit')")
    @Log(title = "浏览记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ViewInfoEdit viewInfoEdit)
    {
        ViewInfo viewInfo = ViewInfoEdit.editToObj(viewInfoEdit);
        return toAjax(viewInfoService.updateViewInfo(viewInfo));
    }

    /**
     * 删除浏览记录
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:remove')")
    @Log(title = "浏览记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(viewInfoService.deleteViewInfoByIds(ids));
    }

    /**
     * 导入浏览记录数据
     */
    @PreAuthorize("@ss.hasPermi('manage:viewInfo:import')")
    @Log(title = "浏览记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<ViewInfo> util = new ExcelUtil<ViewInfo>(ViewInfo.class);
        List<ViewInfo> viewInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = viewInfoService.importViewInfoData(viewInfoList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载浏览记录导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<ViewInfo> util = new ExcelUtil<ViewInfo>(ViewInfo.class);
        util.importTemplateExcel(response, "浏览记录数据");
    }
}
