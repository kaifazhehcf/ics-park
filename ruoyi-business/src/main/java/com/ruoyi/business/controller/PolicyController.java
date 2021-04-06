package com.ruoyi.business.controller;

import com.google.common.collect.Maps;
import com.ruoyi.business.domain.Policy;
import com.ruoyi.business.service.IPolicyService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ValidatorUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.controller.BaseController;

import java.util.List;
import java.util.Map;


/**
 * 政策管理 提供者
 *
 * @author zzm
 * @date 2020-10-29
 */
@RestController
@RequestMapping("/business/policy")
public class PolicyController extends BaseController
{

    @Autowired
    private IPolicyService policyService;

    /**
     * 查询${tableComment}
     */
    @GetMapping("get/{id}")
    public Policy get(@PathVariable("id") Long id)
    {
        return policyService.selectPolicyById(id);

    }

    /**
     * 查询政策管理列表
     */
    @GetMapping("list")
    public R list(Policy policy)
    {
        startPage();
        policy.setParkId(getParkId());
        return result(policyService.selectPolicyList(policy));
    }



    /**
     * 新增保存政策管理
     */
    @PostMapping("save")
    public R addSave(@RequestBody Policy policy)
    {
        ValidatorUtils.validateEntity(policy);
        policy.setDelFlag(false);
        policy.setParkId(getParkId());
        policy.setIsMarketable(false);
        policy.setCreateBy(getLoginName());
        return toAjax(policyService.insertPolicy(policy));
    }

    /**
     * 修改保存政策管理
     */
    @PostMapping("update")
    public R editSave(@RequestBody Policy policy)
    {
        ValidatorUtils.validateEntity(policy);
        policy.setUpdateBy(getLoginName());
        return toAjax(policyService.updatePolicy(policy));
    }

    /**
     * 删除${tableComment}
     */
    @PostMapping("remove")
    public R remove(String ids)
    {
        return toAjax(policyService.deletePolicyByIds(ids));
    }


    /**
     * 上架、下架
     */
    @PostMapping("changeMarketable")
    public R changeMarketable(@RequestBody Policy policy) {
        Policy newPolicy = policyService.selectPolicyById(policy.getId());
        if (newPolicy == null) {
            return R.error("政策不存在!");
        }
        policy.setMarketableTime(DateUtils.getNowDate());
        return toAjax(policyService.update(policy));
    }


}
