package com.ruoyi.business.controller;

import com.google.common.collect.Maps;
import com.ruoyi.business.domain.DjBanner;
import com.ruoyi.business.service.IDjBannerService;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
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
 * 党建banner 提供者
 *
 * @author zzm
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/business/djBanner")
public class DjBannerController extends BaseController
{

    @Autowired
    private IDjBannerService djBannerService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询${tableComment}
     */
    @GetMapping("get/{id}")
    public DjBanner get(@PathVariable("id") Long id)
    {
        return djBannerService.selectDjBannerById(id);

    }

    /**
     * 查询党建banner列表
     */
    @GetMapping("list")
    public R list(DjBanner djBanner)
    {
        startPage();
        djBanner.setParkId(getParkId());
        // 默认点击量为0
        djBanner.setHits(0l);
        return result(djBannerService.selectDjBannerList(djBanner));
    }


    /**
     * 新增保存党建banner
     */
    @PostMapping("save")
    public R addSave(@RequestBody DjBanner djBanner)
    {
        ValidatorUtils.validateEntity(djBanner);
        djBanner.setDelFlag(false);
        djBanner.setParkId(getParkId());
        djBanner.setCreateBy(getLoginName());
        djBanner.setIsMarketable(false);
        djBanner.setHits(0L);
        return toAjax(djBannerService.insertDjBanner(djBanner));
    }

    /**
     * 修改保存党建banner
     */
    @PostMapping("update")
    public R editSave(@RequestBody DjBanner djBanner)
    {
        ValidatorUtils.validateEntity(djBanner);
        djBanner.setUpdateBy(getLoginName());
        return toAjax(djBannerService.updateDjBanner(djBanner));
    }

    /**
     * 删除${tableComment}
     */
    @PostMapping("remove")
    public R remove(String ids)
    {
        return toAjax(djBannerService.deleteDjBannerByIds(ids));
    }


    /**
     * 上架、下架
     */
    @PostMapping("changeMarketable")
    public R changeMarketable(@RequestBody DjBanner djBanner) {
        DjBanner newDjBanner = djBannerService.selectDjBannerById(djBanner.getId());
        if (newDjBanner == null) {
            return R.error("党建banner不存在!");
        }
        djBanner.setMarketableTime(DateUtils.getNowDate());
        return toAjax(djBannerService.update(djBanner));
    }

}