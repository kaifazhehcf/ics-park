package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.business.domain.DjBanner;
import com.ruoyi.business.mapper.DjBannerMapper;
import com.ruoyi.business.service.IDjBannerService;
import com.ruoyi.common.core.service.impl.IBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 党建bannerService业务层处理
 *
 * @author zzm
 * @date 2020-10-27
 */
@Service
public class DjBannerServiceImpl extends IBaseServiceImpl<DjBanner> implements IDjBannerService {
    @Autowired
    private DjBannerMapper djBannerMapper;

    /**
     * 查询党建banner
     *
     * @param id 党建bannerID
     * @return 党建banner
     */
    @Override
    public DjBanner selectDjBannerById(Long id) {
        return djBannerMapper.selectDjBannerById(id);
    }

    /**
     * 查询党建banner列表
     *
     * @param djBanner 党建banner
     * @return 党建banner
     */
    @Override
    public List<DjBanner> selectDjBannerList(DjBanner djBanner) {
        return djBannerMapper.selectDjBannerList(djBanner);
    }

    /**
     * 新增党建banner
     *
     * @param djBanner 党建banner
     * @return 结果
     */
    @Override
    public int insertDjBanner(DjBanner djBanner) {
        djBanner.setCreateTime(DateUtils.getNowDate());
        return this.save(djBanner);
    }

    /**
     * 修改党建banner
     *
     * @param djBanner 党建banner
     * @return 结果
     */
    @Override
    public int updateDjBanner(DjBanner djBanner) {
        djBanner.setUpdateTime(DateUtils.getNowDate());
        return this.update(djBanner);
    }

    /**
     * 删除党建banner对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDjBannerByIds(String ids) {
        return this.delete(ids);
    }

    /**
     * 删除党建banner信息
     *
     * @param id 党建bannerID
     * @return 结果
     */
    @Override
    public int deleteDjBannerById(Long id) {
        return this.delete(id);
    }
}