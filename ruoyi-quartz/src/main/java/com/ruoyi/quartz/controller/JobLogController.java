package com.ruoyi.quartz.controller;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.JobLog;
import com.ruoyi.quartz.service.IJobLogService;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/jobLog")
public class JobLogController extends BaseController {

    @Autowired
    private IJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @HasPermissions("monitor:job:list")
    @GetMapping("/list")
    public TableDataInfo list(JobLog sysJobLog) {
        startPage();
        List<JobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @HasPermissions("monitor:job:export")
    @OperLog(title = "任务调度日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(JobLog sysJobLog) {
        List<JobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<JobLog> util = new ExcelUtil<JobLog>(JobLog.class);
        return util.exportExcel(list, "调度日志");
    }

    /**
     * 根据调度编号获取详细信息
     */
    @HasPermissions("monitor:job:query")
    @GetMapping(value = "/{configId}")
    public R getInfo(@PathVariable Long jobLogId) {
        return R.data(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     */
    @HasPermissions("monitor:job:remove")
    @OperLog(title = "定时任务调度日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public R remove(@PathVariable Long[] jobLogIds) {
        return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @HasPermissions("monitor:job:remove")
    @OperLog(title = "调度日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R clean() {
        jobLogService.cleanJobLog();
        return R.ok();
    }
}
