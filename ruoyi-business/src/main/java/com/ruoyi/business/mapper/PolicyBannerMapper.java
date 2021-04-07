package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.PolicyBanner;
import com.ruoyi.common.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 政策bannerMapper接口
 *
 * @author zzm
 * @date 2020-10-29
 */
@Mapper
public interface PolicyBannerMapper extends BaseMapper<PolicyBanner> {
    /**
     * 查询政策banner
     *
     * @param id 政策bannerID
     * @return 政策banner
     */
    PolicyBanner selectPolicyBannerById(Long id);

    /**
     * 查询政策banner列表
     *
     * @param policyBanner 政策banner
     * @return 政策banner集合
     */
    List<PolicyBanner> selectPolicyBannerList(PolicyBanner policyBanner);

}