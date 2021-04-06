package com.ruoyi.business.controller;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.business.domain.ExpenseSettings;
import com.ruoyi.business.service.IExpenseSettingsService;

/**
 * 费项配置 提供者
 *
 * @author Jack
 * @date 2020-11-12
 */
@RestController
@RequestMapping("/business/expenseSettings")
public class ExpenseSettingsController extends BaseController {

    @Autowired
    private IExpenseSettingsService expenseSettingsService;

    /**
     * 查询费项配置
     */
    @GetMapping("get/{id}")
    public ExpenseSettings get(@PathVariable("id") Long id) {
        return expenseSettingsService.selectExpenseSettingsById(id);
    }

    /**
     * 查询费项配置列表
     */
    @GetMapping("list")
    public R list(ExpenseSettings expenseSettings) {
        startPage();
        return result(expenseSettingsService.selectExpenseSettingsList(expenseSettings));
    }

    /**
     * 新增保存费项配置
     */
    @PostMapping("save")
    public R addSave(@RequestBody ExpenseSettings expenseSettings) {
        expenseSettings.setIsEnabled(false);
        expenseSettings.setCreateBy(getLoginName());
        return toAjax(expenseSettingsService.insertExpenseSettings(expenseSettings));
    }

    /**
     * 修改保存费项配置
     */
    @PostMapping("update")
    public R editSave(@RequestBody ExpenseSettings expenseSettings) {
        return toAjax(expenseSettingsService.updateExpenseSettings(expenseSettings));
    }

    /**
     * 启动 ，停用
     */
    @PostMapping("enabled")
    public R changeMarketable(@RequestBody ExpenseSettings expenseSettings) {
        ExpenseSettings newExpenseSettings = expenseSettingsService.selectExpenseSettingsById(expenseSettings.getId());
        if (newExpenseSettings == null) {
            return R.error("数据不存在!");
        }
        expenseSettings.setUpdateBy(getLoginName());
        expenseSettings.setUpdateTime(DateUtils.getNowDate());
        return toAjax(expenseSettingsService.update(expenseSettings));
    }

    /**
     * 删除费项配置
     */
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(expenseSettingsService.deleteExpenseSettingsByIds(ids));
    }
}
