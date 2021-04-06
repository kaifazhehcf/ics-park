package com.ruoyi.business.service;

import com.ruoyi.business.domain.DjBanner;
import com.ruoyi.common.core.service.IBaseService;

import java.util.List;

/**
 * 党建bannerService接口
 *
 * @author zzm
 * @date 2020-10-27
 */
public interface IDjBannerService extends IBaseService<DjBanner>
{
    /**
     * 查询党建banner
     *
     * @param id 党建bannerID
     * @return 党建banner
     */
    DjBanner selectDjBannerById(Long id);

    /**
     * 查询党建banner列表
     *
     * @param djBanner 党建banner
     * @return 党建banner集合
     */
    List<DjBanner> selectDjBannerList(DjBanner djBanner);

    /**
     * 新增党建banner
     *
     * @param djBanner 党建banner
     * @return 结果
     */
    int insertDjBanner(DjBanner djBanner);

    /**
     * 修改党建banner
     *
     * @param djBanner 党建banner
     * @return 结果
     */
    int updateDjBanner(DjBanner djBanner);

    /**
     * 批量删除党建banner
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDjBannerByIds(String ids);

    /**
     * 删除党建banner信息
     *
     * @param id 党建bannerID
     * @return 结果
     */
    int deleteDjBannerById(Long id);
}
