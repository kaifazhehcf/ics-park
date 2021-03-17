package com.ruoyi.controller.monitor;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("monitor/operLog")
public class OperLogController extends BaseController {
    @Autowired
    private IOperLogService operLogService;

    /**
     * 查询操作日志记录
     */
    @GetMapping("get/{operId}")
    public OperLog get(@PathVariable("operId") Long operId) {
        return operLogService.selectOperLogById(operId);
    }

    /**
     * 查询操作日志记录列表
     */
    @HasPermissions("monitor:operlog:list")
    @RequestMapping("list")
    public R list(OperLog operLog) {
        startPage();
        return result(operLogService.selectOperLogList(operLog));
    }

    @com.ruoyi.system.log.annotation.OperLog(title = "操作日志", businessType = BusinessType.EXPORT)
    @HasPermissions("monitor:operlog:export")
    @PostMapping("/export")
    public R export(OperLog operLog) {
        List<OperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<OperLog> util = new ExcelUtil<OperLog>(OperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    /**
     * 新增保存操作日志记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody OperLog operLog) {
        operLogService.insertOperlog(operLog);
    }

    /**
     * 删除操作日志记录
     */
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(operLogService.deleteOperLogByIds(ids));
    }

    @com.ruoyi.system.log.annotation.OperLog(title = "操作日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    public R clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }
}
