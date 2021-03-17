package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.DictType;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典类型 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/dict/type")
public class DictTypeController extends BaseController {

    @Autowired
    private IDictTypeService dictTypeService;

    /**
     * 查询字典类型
     */
    @GetMapping("get/{dictId}")
    public DictType get(@PathVariable("dictId") Long dictId) {
        return dictTypeService.selectDictTypeById(dictId);
    }

    /**
     * 查询字典类型列表
     */
    @GetMapping("list")
    @HasPermissions("system:dict:list")
    public R list(DictType dictType, PageDomain page) {
        startPage();
        return result(dictTypeService.selectDictTypeList(dictType));
    }


    /**
     * 新增保存字典类型
     */
    @OperLog(title = "字典类型", businessType = BusinessType.INSERT)
    @HasPermissions("system:dict:add")
    @PostMapping("save")
    public R addSave(@RequestBody DictType dictType) {
        dictType.setParkId(getParkId());
        dictType.setCreateBy(getLoginName());
        return toAjax(dictTypeService.insertDictType(dictType));
    }

    /**
     * 修改保存字典类型
     */
    @OperLog(title = "字典类型", businessType = BusinessType.UPDATE)
    @HasPermissions("system:dict:edit")
    @PostMapping("update")
    public R editSave(@RequestBody DictType dictType) {
        dictType.setUpdateBy(getLoginName());
        return toAjax(dictTypeService.updateDictType(dictType));
    }

    /**
     * 删除字典类型
     *
     * @throws Exception
     */
    @OperLog(title = "字典类型", businessType = BusinessType.DELETE)
    @HasPermissions("system:dict:remove")
    @PostMapping("remove")
    public R remove(String ids) throws Exception {
        return toAjax(dictTypeService.deleteDictTypeByIds(ids));
    }

}
