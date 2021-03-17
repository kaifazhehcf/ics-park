package com.ruoyi.quartz.util;

import com.ruoyi.quartz.domain.Job;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author ruoyi
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
