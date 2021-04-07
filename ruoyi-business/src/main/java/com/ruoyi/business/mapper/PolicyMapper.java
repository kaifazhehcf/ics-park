package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.Policy;
import com.ruoyi.common.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 政策管理Mapper接口
 *
 * @author zzm
 * @date 2020-10-29
 */
@Mapper
public interface PolicyMapper extends BaseMapper<Policy> {
    /**
     * 查询政策管理
     *
     * @param id 政策管理ID
     * @return 政策管理
     */
    Policy selectPolicyById(Long id);

    /**
     * 查询政策管理列表
     *
     * @param policy 政策管理
     * @return 政策管理集合
     */
    List<Policy> selectPolicyList(Policy policy);

}