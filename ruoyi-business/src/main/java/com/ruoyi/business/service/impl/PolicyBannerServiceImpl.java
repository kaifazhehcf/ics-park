package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.business.domain.PolicyBanner;
import com.ruoyi.business.mapper.PolicyBannerMapper;
import com.ruoyi.business.service.IPolicyBannerService;
import com.ruoyi.common.core.service.impl.IBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 政策bannerService业务层处理
 *
 * @author zzm
 * @date 2020-10-29
 */
@Service
public class PolicyBannerServiceImpl extends IBaseServiceImpl<PolicyBanner> implements IPolicyBannerService {
    @Autowired
    private PolicyBannerMapper policyBannerMapper;

    /**
     * 查询政策banner
     *
     * @param id 政策bannerID
     * @return 政策banner
     */
    @Override
    public PolicyBanner selectPolicyBannerById(Long id) {
        return policyBannerMapper.selectPolicyBannerById(id);
    }

    /**
     * 查询政策banner列表
     *
     * @param policyBanner 政策banner
     * @return 政策banner
     */
    @Override
    public List<PolicyBanner> selectPolicyBannerList(PolicyBanner policyBanner) {
        return policyBannerMapper.selectPolicyBannerList(policyBanner);
    }

    /**
     * 新增政策banner
     *
     * @param policyBanner 政策banner
     * @return 结果
     */
    @Override
    public int insertPolicyBanner(PolicyBanner policyBanner) {
        policyBanner.setCreateTime(DateUtils.getNowDate());
        return this.save(policyBanner);
    }

    /**
     * 修改政策banner
     *
     * @param policyBanner 政策banner
     * @return 结果
     */
    @Override
    public int updatePolicyBanner(PolicyBanner policyBanner) {
        policyBanner.setUpdateTime(DateUtils.getNowDate());
        return this.update(policyBanner);
    }

    /**
     * 删除政策banner对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePolicyBannerByIds(String ids) {
        return this.delete(ids);
    }

    /**
     * 删除政策banner信息
     *
     * @param id 政策bannerID
     * @return 结果
     */
    @Override
    public int deletePolicyBannerById(Long id) {
        return this.delete(id);
    }
}