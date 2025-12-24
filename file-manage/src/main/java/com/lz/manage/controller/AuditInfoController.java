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
import com.lz.manage.model.domain.AuditInfo;
import com.lz.manage.model.vo.auditInfo.AuditInfoVo;
import com.lz.manage.model.dto.auditInfo.AuditInfoQuery;
import com.lz.manage.model.dto.auditInfo.AuditInfoInsert;
import com.lz.manage.model.dto.auditInfo.AuditInfoEdit;
import com.lz.manage.service.IAuditInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 审核信息Controller
 *
 * @author YY
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/manage/auditInfo")
public class AuditInfoController extends BaseController
{
    @Resource
    private IAuditInfoService auditInfoService;

    /**
     * 查询审核信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(AuditInfoQuery auditInfoQuery)
    {
        AuditInfo auditInfo = AuditInfoQuery.queryToObj(auditInfoQuery);
        startPage();
        List<AuditInfo> list = auditInfoService.selectAuditInfoList(auditInfo);
        List<AuditInfoVo> listVo= list.stream().map(AuditInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出审核信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:export')")
    @Log(title = "审核信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuditInfoQuery auditInfoQuery)
    {
        AuditInfo auditInfo = AuditInfoQuery.queryToObj(auditInfoQuery);
        List<AuditInfo> list = auditInfoService.selectAuditInfoList(auditInfo);
        ExcelUtil<AuditInfo> util = new ExcelUtil<AuditInfo>(AuditInfo.class);
        util.exportExcel(response, list, "审核信息数据");
    }

    /**
     * 获取审核信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        AuditInfo auditInfo = auditInfoService.selectAuditInfoById(id);
        return success(AuditInfoVo.objToVo(auditInfo));
    }

    /**
     * 新增审核信息
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:add')")
    @Log(title = "审核信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuditInfoInsert auditInfoInsert)
    {
        AuditInfo auditInfo = AuditInfoInsert.insertToObj(auditInfoInsert);
        return toAjax(auditInfoService.insertAuditInfo(auditInfo));
    }

    /**
     * 修改审核信息
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:edit')")
    @Log(title = "审核信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuditInfoEdit auditInfoEdit)
    {
        AuditInfo auditInfo = AuditInfoEdit.editToObj(auditInfoEdit);
        return toAjax(auditInfoService.updateAuditInfo(auditInfo));
    }

    /**
     * 删除审核信息
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:remove')")
    @Log(title = "审核信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(auditInfoService.deleteAuditInfoByIds(ids));
    }

    /**
     * 导入审核信息数据
     */
    @PreAuthorize("@ss.hasPermi('manage:auditInfo:import')")
    @Log(title = "审核信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<AuditInfo> util = new ExcelUtil<AuditInfo>(AuditInfo.class);
        List<AuditInfo> auditInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = auditInfoService.importAuditInfoData(auditInfoList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载审核信息导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<AuditInfo> util = new ExcelUtil<AuditInfo>(AuditInfo.class);
        util.importTemplateExcel(response, "审核信息数据");
    }
}
