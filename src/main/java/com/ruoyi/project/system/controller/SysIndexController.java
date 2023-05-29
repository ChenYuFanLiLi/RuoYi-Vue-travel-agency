package com.ruoyi.project.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mchange.lang.IntegerUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.domain.Itinerary;
import com.ruoyi.travel.service.ICustomerService;
import com.ruoyi.travel.service.IItineraryService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.RuoYiConfig;

import java.util.*;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IItineraryService itineraryService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }

    @GetMapping("/index/getItineraryLineChart")
    public AjaxResult getItineraryLineChart(){
        QueryWrapper<Itinerary> itineraryQueryWrapper = new QueryWrapper<>();
        itineraryQueryWrapper.select("MONTH(departure_date) as month,count(id) as num");
        itineraryQueryWrapper.apply("YEAR(departure_date) = YEAR(NOW())").groupBy("MONTH(departure_date)");
        List<Map<String, Object>> maps = itineraryService.listMaps(itineraryQueryWrapper);
        ArrayList<String> month = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();
        Integer monthNumber = 1;
        for (Map<String, Object> item : maps) {
            monthNumber = populateMonth(item, monthNumber, month, num);
            month.add(item.get("month").toString() + "月");
            num.add(Integer.parseInt(item.get("num").toString()));
        }
        HashMap<String, Object> itineraryLineChart = new HashMap<>();
        itineraryLineChart.put("month",month);
        itineraryLineChart.put("num",num);
        return AjaxResult.success(itineraryLineChart);
    }

    @GetMapping("/index/getCustomerLineChart")
    public AjaxResult getCustomerLineChart(){
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.select("MONTH(create_time) as month, count(id) as num");
        customerQueryWrapper.apply("YEAR(create_time) = YEAR(NOW())").groupBy("MONTH(create_time)");
        List<Map<String, Object>> maps = customerService.listMaps(customerQueryWrapper);
        ArrayList<String> month = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();
        Integer monthNumber = 1;
        for (Map<String, Object> item : maps) {
            monthNumber = populateMonth(item, monthNumber, month, num);
            month.add(item.get("month").toString() + "月");
            num.add(Integer.parseInt(item.get("num").toString()));
        }
        HashMap<String, Object> customerLineChart = new HashMap<>();
        customerLineChart.put("month",month);
        customerLineChart.put("num",num);
        return AjaxResult.success(customerLineChart);
    }
    private Integer populateMonth(Map<String,Object> item, Integer monthNumber,List<String> month,List<Integer> num){
        if ((Integer)item.get("month")>monthNumber){
            month.add(monthNumber.toString()+"月");
            num.add(0);
            return populateMonth(item,++monthNumber,month,num);
        }else {
            return ++monthNumber;
        }
    }


}
