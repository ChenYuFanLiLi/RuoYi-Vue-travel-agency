package com.ruoyi.travel.controller;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.travel.domain.ScheduleRouting;
import com.ruoyi.travel.dto.ScheduleDTO;
import com.ruoyi.travel.service.IScheduleRoutingService;
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
import com.ruoyi.travel.domain.Schedule;
import com.ruoyi.travel.service.IScheduleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 线路信息管理Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-28
 */
@RestController
@RequestMapping("/travel/schedule")
@Api(value = "线路信息管理控制器", tags = {"线路信息管理管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduleController extends BaseController
{
    private final IScheduleService scheduleService;

    private final IScheduleRoutingService scheduleRoutingService;

    /**
     * 查询线路信息管理列表
     */
    @ApiOperation("查询线路信息管理列表")
    @PreAuthorize("@ss.hasPermi('travel:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(Schedule schedule) {
        startPage();
        List<Schedule> list = scheduleService.list(new QueryWrapper<Schedule>(schedule));
        return getDataTable(list);
    }

    /**
     * 导出线路信息管理列表
     */
    @ApiOperation("导出线路信息管理列表")
    @PreAuthorize("@ss.hasPermi('travel:schedule:export')")
    @Log(title = "线路信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,Schedule schedule) {
        List<Schedule> list = scheduleService.list(new QueryWrapper<Schedule>(schedule));
        ExcelUtil<Schedule> util = new ExcelUtil<Schedule>(Schedule.class);
        util.exportExcel(response ,list, "线路信息管理数据");
    }

    /**
     * 获取线路信息管理详细信息
     */
    @ApiOperation("获取线路信息管理详细信息")
    @PreAuthorize("@ss.hasPermi('travel:schedule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        Schedule schedule = scheduleService.getById(id);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule,scheduleDTO);
        QueryWrapper<ScheduleRouting> scheduleRoutingQueryWrapper = new QueryWrapper<>();
        scheduleRoutingQueryWrapper.eq("schedule_id",schedule.getId());
        scheduleDTO.setScheduleRoutingList(scheduleRoutingService.list(scheduleRoutingQueryWrapper));
        return AjaxResult.success(scheduleDTO);
    }

    /**
     * 新增线路信息管理
     */
    @ApiOperation("新增线路信息管理")
    @PreAuthorize("@ss.hasPermi('travel:schedule:add')")
    @Log(title = "线路信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        boolean save = scheduleService.save(schedule);
        List<ScheduleRouting> scheduleRoutingList = scheduleDTO.getScheduleRoutingList().stream().peek((item)-> item.setScheduleId(schedule.getId())).collect(Collectors.toList());
        scheduleRoutingService.saveBatch(scheduleRoutingList);
        return toAjax(save);
    }

    /**
     * 修改线路信息管理
     */
    @ApiOperation("修改线路信息管理")
    @PreAuthorize("@ss.hasPermi('travel:schedule:edit')")
    @Log(title = "线路信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        List<ScheduleRouting> scheduleRoutingList = scheduleDTO.getScheduleRoutingList().stream().peek(item->item.setScheduleId(scheduleDTO.getId())).collect(Collectors.toList());
        scheduleRoutingService.updateBatchById(scheduleRoutingList);
        return toAjax(scheduleService.updateById(schedule));
    }

    /**
     * 删除线路信息管理
     */
    @ApiOperation("删除线路信息管理")
    @PreAuthorize("@ss.hasPermi('travel:schedule:remove')")
    @Log(title = "线路信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        scheduleRoutingService.removeByScheduleIds(ids);
        return toAjax(scheduleService.removeByIds(Arrays.asList(ids)));
    }
}
