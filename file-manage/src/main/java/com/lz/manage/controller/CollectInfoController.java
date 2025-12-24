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
import com.lz.manage.model.domain.CollectInfo;
import com.lz.manage.model.vo.collectInfo.CollectInfoVo;
import com.lz.manage.model.dto.collectInfo.CollectInfoQuery;
import com.lz.manage.model.dto.collectInfo.CollectInfoInsert;
import com.lz.manage.model.dto.collectInfo.CollectInfoEdit;
import com.lz.manage.service.ICollectInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 收藏记录Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/collectInfo")
public class CollectInfoController extends BaseController
{
    @Resource
    private ICollectInfoService collectInfoService;

    /**
     * 查询收藏记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CollectInfoQuery collectInfoQuery)
    {
        CollectInfo collectInfo = CollectInfoQuery.queryToObj(collectInfoQuery);
        startPage();
        List<CollectInfo> list = collectInfoService.selectCollectInfoList(collectInfo);
        List<CollectInfoVo> listVo= list.stream().map(CollectInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出收藏记录列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:export')")
    @Log(title = "收藏记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CollectInfoQuery collectInfoQuery)
    {
        CollectInfo collectInfo = CollectInfoQuery.queryToObj(collectInfoQuery);
        List<CollectInfo> list = collectInfoService.selectCollectInfoList(collectInfo);
        ExcelUtil<CollectInfo> util = new ExcelUtil<CollectInfo>(CollectInfo.class);
        util.exportExcel(response, list, "收藏记录数据");
    }

    /**
     * 获取收藏记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        CollectInfo collectInfo = collectInfoService.selectCollectInfoById(id);
        return success(CollectInfoVo.objToVo(collectInfo));
    }

    /**
     * 新增收藏记录
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:add')")
    @Log(title = "收藏记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CollectInfoInsert collectInfoInsert)
    {
        CollectInfo collectInfo = CollectInfoInsert.insertToObj(collectInfoInsert);
        return toAjax(collectInfoService.insertCollectInfo(collectInfo));
    }

    /**
     * 修改收藏记录
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:edit')")
    @Log(title = "收藏记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CollectInfoEdit collectInfoEdit)
    {
        CollectInfo collectInfo = CollectInfoEdit.editToObj(collectInfoEdit);
        return toAjax(collectInfoService.updateCollectInfo(collectInfo));
    }

    /**
     * 删除收藏记录
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:remove')")
    @Log(title = "收藏记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(collectInfoService.deleteCollectInfoByIds(ids));
    }

    /**
     * 导入收藏记录数据
     */
    @PreAuthorize("@ss.hasPermi('manage:collectInfo:import')")
    @Log(title = "收藏记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CollectInfo> util = new ExcelUtil<CollectInfo>(CollectInfo.class);
        List<CollectInfo> collectInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = collectInfoService.importCollectInfoData(collectInfoList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载收藏记录导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<CollectInfo> util = new ExcelUtil<CollectInfo>(CollectInfo.class);
        util.importTemplateExcel(response, "收藏记录数据");
    }
}
