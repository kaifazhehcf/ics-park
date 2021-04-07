package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.business.domain.Dj;
import com.ruoyi.business.mapper.DjMapper;
import com.ruoyi.business.service.IDjService;
import com.ruoyi.common.core.service.impl.IBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 党建管理Service业务层处理
 *
 * @author zzm
 * @date 2020-10-27
 */
@Service
public class DjServiceImpl extends IBaseServiceImpl<Dj> implements IDjService {
    @Autowired
    private DjMapper djMapper;

    /**
     * 查询党建管理
     *
     * @param id 党建管理ID
     * @return 党建管理
     */
    @Override
    public Dj selectDjById(Long id) {
        return djMapper.selectDjById(id);
    }

    /**
     * 查询党建管理列表
     *
     * @param dj 党建管理
     * @return 党建管理
     */
    @Override
    public List<Dj> selectDjList(Dj dj) {
        return djMapper.selectDjList(dj);
    }

    /**
     * 新增党建管理
     *
     * @param dj 党建管理
     * @return 结果
     */
    @Override
    public int insertDj(Dj dj) {
        dj.setCreateTime(DateUtils.getNowDate());
        return this.save(dj);
    }

    /**
     * 修改党建管理
     *
     * @param dj 党建管理
     * @return 结果
     */
    @Override
    public int updateDj(Dj dj) {
        dj.setUpdateTime(DateUtils.getNowDate());
        return this.update(dj);
    }

    /**
     * 删除党建管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDjByIds(String ids) {
        return this.delete(ids);
    }

    /**
     * 删除党建管理信息
     *
     * @param id 党建管理ID
     * @return 结果
     */
    @Override
    public int deleteDjById(Long id) {
        return this.delete(id);
    }
}
