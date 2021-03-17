package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.DictData;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/dict/data")
public class DictDataController extends BaseController {

    @Autowired
    private IDictDataService dictDataService;

    /**
     * 查询字典数据
     */
    @GetMapping("get/{dictCode}")
    public DictData get(@PathVariable("dictCode") Long dictCode) {
        return dictDataService.selectDictDataById(dictCode);
    }

    /**
     * 查询字典数据列表
     */
    @GetMapping("list")
    @HasPermissions("system:dict:list")
    public R list(DictData dictData) {
        startPage();
        return result(dictDataService.selectDictDataList(dictData));
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 参数键值
     */
    @GetMapping("type")
    public List<DictData> getType(String dictType) {
        return dictDataService.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @GetMapping("label")
    public String getLabel(String dictType, String dictValue) {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }


    /**
     * 新增保存字典数据
     */
    @OperLog(title = "字典数据", businessType = BusinessType.INSERT)
    @HasPermissions("system:dict:add")
    @PostMapping("save")
    public R addSave(@RequestBody DictData dictData) {
        dictData.setParkId(getParkId());
        dictData.setCreateBy(getLoginName());
        return toAjax(dictDataService.insertDictData(dictData));
    }

    /**
     * 修改保存字典数据
     */
    @OperLog(title = "字典数据", businessType = BusinessType.UPDATE)
    @HasPermissions("system:dict:edit")
    @PostMapping("update")
    public R editSave(@RequestBody DictData dictData) {
        dictData.setUpdateBy(getLoginName());
        return toAjax(dictDataService.updateDictData(dictData));
    }

    /**
     * 删除字典数据
     */
    @OperLog(title = "字典数据", businessType = BusinessType.DELETE)
    @HasPermissions("system:dict:remove")
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(dictDataService.deleteDictDataByIds(ids));
    }

}
