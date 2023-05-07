package com.ruoyi.travel.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 项目对象 travel_project
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_project")
public class Project extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目拟单价 */
    @Excel(name = "项目拟单价")
    private BigDecimal projectSuggestedPrice;

    /** 项目成本 */
    @Excel(name = "项目成本")
    private BigDecimal projectCost;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String projectType;

    /** 项目单位 */
    @Excel(name = "项目单位")
    private String projectUnit;

}
