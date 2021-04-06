package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.DjBanner;
import com.ruoyi.common.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 党建bannerMapper接口
 *
 * @author zzm
 * @date 2020-10-27
 */
@Mapper
public interface DjBannerMapper extends BaseMapper<DjBanner>
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

}
