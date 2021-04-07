package com.ruoyi.business.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 费项配置对象 ics_expense_settings
 *
 * @author Jack
 * @date 2020-11-12
 */
@Data
@Table(name = "ics_expense_settings")
public class ExpenseSettings extends BaseEntity<ExpenseSettings> {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 费项名称
     */
    private String name;

    /**
     * 费项类型（0-系统费项，1-周期性费项，2-一次性费项）
     */
    private String type;

    /**
     * 计量单位（度，平方米，吨，立方米，千克）
     */
    private String unitsCode;

    /**
     * 税率%
     */
    private Double taxFee;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否启用（）
     */
    private Boolean isEnabled;

}
