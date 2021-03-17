package com.ruoyi.quartz.mapper;

import com.ruoyi.common.core.dao.BaseMapper;
import com.ruoyi.quartz.domain.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {
    /**
     * 查询调度任务日志集合
     *
     * @param job 调度信息
     * @return 操作日志集合
     */
    List<Job> selectJobList(Job job);

    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    List<Job> selectJobAll();

    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteJobByIds(Long[] ids);

}
