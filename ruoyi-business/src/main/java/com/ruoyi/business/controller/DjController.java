package com.ruoyi.business.controller;

import com.ruoyi.business.domain.Dj;
import com.ruoyi.business.service.IDjService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.controller.BaseController;


/**
 * 党建管理 提供者
 *
 * @author zzm
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/business/dj")
public class DjController extends BaseController {

    @Autowired
    private IDjService djService;

    /**
     * 查询${tableComment}
     */
    @GetMapping("get/{id}")
    public Dj get(@PathVariable("id") Long id) {
        return djService.selectDjById(id);
    }

    /**
     * 查询党建管理列表
     */
    @GetMapping("list")
    public R list(Dj dj) {
        startPage();
        dj.setParkId(getParkId());
        return result(djService.selectDjList(dj));
    }


    /**
     * 新增保存党建管理
     */
    @PostMapping("save")
    public R addSave(@RequestBody Dj dj) {
        ValidatorUtils.validateEntity(dj);
        dj.setDelFlag(false);
        dj.setParkId(getParkId());
        dj.setIsMarketable(false);
        dj.setCreateBy(getLoginName());
        return toAjax(djService.insertDj(dj));
    }

    /**
     * 修改保存党建管理
     */
    @PostMapping("update")
    public R editSave(@RequestBody Dj dj) {
        ValidatorUtils.validateEntity(dj);
        dj.setUpdateBy(getLoginName());
        return toAjax(djService.updateDj(dj));
    }

    /**
     * 删除${tableComment}
     */
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(djService.deleteDjByIds(ids));
    }


    /**
     * 上架、下架
     */
    @PostMapping("changeMarketable")
    public R changeMarketable(@RequestBody Dj dj) {
        Dj newDj = djService.selectDjById(dj.getId());
        if (newDj == null) {
            return R.error("党建管理不存在!");
        }
        dj.setMarketableTime(DateUtils.getNowDate());
        return toAjax(djService.update(dj));
    }

}
