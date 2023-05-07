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
import com.ruoyi.travel.domain.Group;
import com.ruoyi.travel.service.IGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 组团社信息Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-04
 */
@RestController
@RequestMapping("/travel/group")
@Api(value = "组团社信息控制器", tags = {"组团社信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GroupController extends BaseController
{
    private final IGroupService groupService;

    /**
     * 查询组团社信息列表
     */
    @ApiOperation("查询组团社信息列表")
    @PreAuthorize("@ss.hasPermi('travel:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(Group group) {
        startPage();
        List<Group> list = groupService.list(new QueryWrapper<Group>(group));
        return getDataTable(list);
    }

    /**
     * 导出组团社信息列表
     */
    @ApiOperation("导出组团社信息列表")
    @PreAuthorize("@ss.hasPermi('travel:group:export')")
    @Log(title = "组团社信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Group group) {
        List<Group> list = groupService.list(new QueryWrapper<Group>(group));
        ExcelUtil<Group> util = new ExcelUtil<Group>(Group.class);
        return util.exportExcel(list, "组团社信息数据");
    }

    /**
     * 获取组团社信息详细信息
     */
    @ApiOperation("获取组团社信息详细信息")
    @PreAuthorize("@ss.hasPermi('travel:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(groupService.getById(id));
    }

    /**
     * 新增组团社信息
     */
    @ApiOperation("新增组团社信息")
    @PreAuthorize("@ss.hasPermi('travel:group:add')")
    @Log(title = "组团社信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Group group) {
        groupService.save(group);
        return AjaxResult.success(group);
    }

    /**
     * 修改组团社信息
     */
    @ApiOperation("修改组团社信息")
    @PreAuthorize("@ss.hasPermi('travel:group:edit')")
    @Log(title = "组团社信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Group group) {
        return toAjax(groupService.updateById(group));
    }

    /**
     * 删除组团社信息
     */
    @ApiOperation("删除组团社信息")
    @PreAuthorize("@ss.hasPermi('travel:group:remove')")
    @Log(title = "组团社信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(groupService.removeByIds(Arrays.asList(ids)));
    }
}
