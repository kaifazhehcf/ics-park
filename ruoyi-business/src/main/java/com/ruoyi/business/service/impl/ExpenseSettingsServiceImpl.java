package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.common.core.service.impl.IBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.ExpenseSettingsMapper;
import com.ruoyi.business.domain.ExpenseSettings;
import com.ruoyi.business.service.IExpenseSettingsService;


/**
 * 费项配置Service业务层处理
 * 
 * @author Jack
 * @date 2020-11-12
 */
@Service
public class ExpenseSettingsServiceImpl extends IBaseServiceImpl<ExpenseSettings> implements IExpenseSettingsService
{
    @Autowired
    private ExpenseSettingsMapper expenseSettingsMapper;

    /**
     * 查询费项配置
     * 
     * @param id 费项配置ID
     * @return 费项配置
     */
    @Override
    public ExpenseSettings selectExpenseSettingsById(Long id)
    {
        return expenseSettingsMapper.selectExpenseSettingsById(id);
    }

    /**
     * 查询费项配置列表
     * 
     * @param expenseSettings 费项配置
     * @return 费项配置
     */
    @Override
    public List<ExpenseSettings> selectExpenseSettingsList(ExpenseSettings expenseSettings)
    {
        return expenseSettingsMapper.selectExpenseSettingsList(expenseSettings);
    }

    /**
     * 新增费项配置
     * 
     * @param expenseSettings 费项配置
     * @return 结果
     */
    @Override
    public int insertExpenseSettings(ExpenseSettings expenseSettings)
    {
        expenseSettings.setCreateTime(DateUtils.getNowDate());
        return this.save(expenseSettings);
    }

    /**
     * 修改费项配置
     * 
     * @param expenseSettings 费项配置
     * @return 结果
     */
    @Override
    public int updateExpenseSettings(ExpenseSettings expenseSettings)
    {
        expenseSettings.setUpdateTime(DateUtils.getNowDate());
        return this.update(expenseSettings);
    }

    /**
     * 删除费项配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteExpenseSettingsByIds(String ids)
    {
        return this.delete(ids);
    }

    /**
     * 删除费项配置信息
     * 
     * @param id 费项配置ID
     * @return 结果
     */
    @Override
    public int deleteExpenseSettingsById(Long id)
    {
        return this.delete(id);
    }



}
