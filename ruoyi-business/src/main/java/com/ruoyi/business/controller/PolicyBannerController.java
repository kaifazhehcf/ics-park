package com.ruoyi.business.controller;

import com.ruoyi.business.domain.PolicyBanner;
import com.ruoyi.business.service.IPolicyBannerService;
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
 * 政策banner 提供者
 *
 * @author zzm
 * @date 2020-10-29
 */
@RestController
@RequestMapping("/business/policyBanner")
public class PolicyBannerController extends BaseController {

    @Autowired
    private IPolicyBannerService policyBannerService;

    /**
     * 查询${tableComment}
     */
    @GetMapping("get/{id}")
    public PolicyBanner get(@PathVariable("id") Long id) {
        return policyBannerService.selectPolicyBannerById(id);

    }

    /**
     * 查询政策banner列表
     */
    @GetMapping("list")
    public R list(PolicyBanner policyBanner) {
        startPage();
        policyBanner.setParkId(getParkId());
        // 默认点击量为0
        policyBanner.setHits(0l);
        return result(policyBannerService.selectPolicyBannerList(policyBanner));
    }


    /**
     * 新增保存政策banner
     */
    @PostMapping("save")
    public R addSave(@RequestBody PolicyBanner policyBanner) {
        ValidatorUtils.validateEntity(policyBanner);
        policyBanner.setDelFlag(false);
        policyBanner.setParkId(getParkId());
        policyBanner.setIsMarketable(false);
        policyBanner.setCreateBy(getLoginName());
        policyBanner.setHits(0L);
        return toAjax(policyBannerService.insertPolicyBanner(policyBanner));
    }

    /**
     * 修改保存政策banner
     */
    @PostMapping("update")
    public R editSave(@RequestBody PolicyBanner policyBanner) {
        ValidatorUtils.validateEntity(policyBanner);
        policyBanner.setUpdateBy(getLoginName());
        return toAjax(policyBannerService.updatePolicyBanner(policyBanner));
    }

    /**
     * 删除${tableComment}
     */
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(policyBannerService.deletePolicyBannerByIds(ids));
    }

    /**
     * 上架、下架
     */
    @PostMapping("changeMarketable")
    public R changeMarketable(@RequestBody PolicyBanner policyBanner) {
        PolicyBanner newPolicyBanner = policyBannerService.selectPolicyBannerById(policyBanner.getId());
        if (newPolicyBanner == null) {
            return R.error("政策banner不存在!");
        }
        policyBanner.setMarketableTime(DateUtils.getNowDate());
        return toAjax(policyBannerService.update(policyBanner));
    }

}