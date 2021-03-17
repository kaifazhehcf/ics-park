package com.ruoyi.quartz.service;

import com.ruoyi.common.core.service.IBaseService;
import com.ruoyi.quartz.domain.JobLog;

import java.util.List;

/**
 * 定时任务调度日志信息信息 服务层
 *
 * @author ruoyi
 */
public interface IJobLogService extends IBaseService<JobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<JobLog> selectJobLogList(JobLog jobLog);

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    JobLog selectJobLogById(Long jobLogId);

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的日志ID
     * @return 结果
     */
    int deleteJobLogByIds(Long[] logIds);

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果
     */
    int deleteJobLogById(Long jobId);

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
