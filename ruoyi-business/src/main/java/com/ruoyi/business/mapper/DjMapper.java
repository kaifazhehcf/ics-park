package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.Dj;
import com.ruoyi.common.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 党建管理Mapper接口
 *
 * @author zzm
 * @date 2020-10-27
 */
@Mapper
public interface DjMapper extends BaseMapper<Dj> {
    /**
     * 查询党建管理
     *
     * @param id 党建管理ID
     * @return 党建管理
     */
    Dj selectDjById(Long id);

    /**
     * 查询党建管理列表
     *
     * @param dj 党建管理
     * @return 党建管理集合
     */
    List<Dj> selectDjList(Dj dj);

}
