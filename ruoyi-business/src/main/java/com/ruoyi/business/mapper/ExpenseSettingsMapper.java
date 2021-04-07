package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.ExpenseSettings;
import com.ruoyi.common.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 费项配置Mapper接口
 *
 * @author Jack
 * @date 2020-11-12
 */
@Mapper
public interface ExpenseSettingsMapper extends BaseMapper<ExpenseSettings> {
    /**
     * 查询费项配置
     *
     * @param id 费项配置ID
     * @return 费项配置
     */
    ExpenseSettings selectExpenseSettingsById(Long id);

    /**
     * 查询费项配置列表
     *
     * @param expenseSettings 费项配置
     * @return 费项配置集合
     */
    List<ExpenseSettings> selectExpenseSettingsList(ExpenseSettings expenseSettings);


}
