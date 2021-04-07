package com.ruoyi.business.service;

import com.ruoyi.business.domain.Dj;
import com.ruoyi.common.core.service.IBaseService;

import java.util.List;

/**
 * 党建管理Service接口
 *
 * @author zzm
 * @date 2020-10-27
 */
public interface IDjService extends IBaseService<Dj> {
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

    /**
     * 新增党建管理
     *
     * @param dj 党建管理
     * @return 结果
     */
    int insertDj(Dj dj);

    /**
     * 修改党建管理
     *
     * @param dj 党建管理
     * @return 结果
     */
    int updateDj(Dj dj);

    /**
     * 批量删除党建管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDjByIds(String ids);

    /**
     * 删除党建管理信息
     *
     * @param id 党建管理ID
     * @return 结果
     */
    int deleteDjById(Long id);
}