package com.ruoyi.travel.controller;

import java.util.List;
import java.util.Arrays;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.travel.domain.Project;
import com.ruoyi.travel.service.IProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 项目Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@RestController
@RequestMapping("/travel/project")
@Api(value = "项目控制器", tags = {"项目管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProjectController extends BaseController
{
    private final IProjectService projectService;

    /**
     * 查询项目列表
     */
    @ApiOperation("查询项目列表")
    @PreAuthorize("@ss.hasPermi('travel:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(Project project) {
        startPage();
        List<Project> list = projectService.list(new QueryWrapper<Project>(project));
        return getDataTable(list);
    }

    /**
     * 导出项目列表
     */
    @ApiOperation("导出项目列表")
    @PreAuthorize("@ss.hasPermi('travel:project:export')")
    @Log(title = "项目", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Project project) {
        List<Project> list = projectService.list(new QueryWrapper<Project>(project));
        ExcelUtil<Project> util = new ExcelUtil<Project>(Project.class);
        return util.exportExcel(list, "项目数据");
    }

    /**
     * 获取项目详细信息
     */
    @ApiOperation("获取项目详细信息")
    @PreAuthorize("@ss.hasPermi('travel:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(projectService.getById(id));
    }

    /**
     * 新增项目
     */
    @ApiOperation("新增项目")
    @PreAuthorize("@ss.hasPermi('travel:project:add')")
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Project project) {
        return toAjax(projectService.save(project));
    }

    /**
     * 修改项目
     */
    @ApiOperation("修改项目")
    @PreAuthorize("@ss.hasPermi('travel:project:edit')")
    @Log(title = "项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Project project) {
        return toAjax(projectService.updateById(project));
    }

    /**
     * 删除项目
     */
    @ApiOperation("删除项目")
    @PreAuthorize("@ss.hasPermi('travel:project:remove')")
    @Log(title = "项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(projectService.removeByIds(Arrays.asList(ids)));
    }
}
