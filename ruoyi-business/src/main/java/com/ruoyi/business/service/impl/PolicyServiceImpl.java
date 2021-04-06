package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.business.domain.Policy;
import com.ruoyi.business.mapper.PolicyMapper;
import com.ruoyi.business.service.IPolicyService;
import com.ruoyi.common.core.service.impl.IBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 政策管理Service业务层处理
 *
 * @author zzm
 * @date 2020-10-29
 */
@Service
public class PolicyServiceImpl extends IBaseServiceImpl<Policy> implements IPolicyService
{
    @Autowired
    private PolicyMapper policyMapper;

    /**
     * 查询政策管理
     *
     * @param id 政策管理ID
     * @return 政策管理
     */
    @Override
    public Policy selectPolicyById(Long id)
    {
        return policyMapper.selectPolicyById(id);
    }

    /**
     * 查询政策管理列表
     *
     * @param policy 政策管理
     * @return 政策管理
     */
    @Override
    public List<Policy> selectPolicyList(Policy policy)
    {
        return policyMapper.selectPolicyList(policy);
    }

    /**
     * 新增政策管理
     *
     * @param policy 政策管理
     * @return 结果
     */
    @Override
    public int insertPolicy(Policy policy)
    {
        policy.setCreateTime(DateUtils.getNowDate());
        return this.save(policy);
    }

    /**
     * 修改政策管理
     *
     * @param policy 政策管理
     * @return 结果
     */
    @Override
    public int updatePolicy(Policy policy)
    {
        policy.setUpdateTime(DateUtils.getNowDate());
        return this.update(policy);
    }

    /**
     * 删除政策管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePolicyByIds(String ids)
    {
        return this.delete(ids);
    }

    /**
     * 删除政策管理信息
     *
     * @param id 政策管理ID
     * @return 结果
     */
    @Override
    public int deletePolicyById(Long id)
    {
        return this.delete(id);
    }
}