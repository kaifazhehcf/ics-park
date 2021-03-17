package com.ruoyi.controller.monitor;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.LoginInfo;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.ILoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统访问记录 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("monitor/loginInfo")
public class LoginInfoController extends BaseController {

    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 查询系统访问记录列表
     */
    @GetMapping("list")
    public R list(LoginInfo loginInfo) {
        startPage();
        return result(loginInfoService.selectLogininforList(loginInfo));
    }

    /**
     * 新增保存系统访问记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody LoginInfo loginInfo) {
        loginInfoService.insertLogininfor(loginInfo);
    }


    /**
     * 删除系统访问记录
     */
    @OperLog(title = "访问日志", businessType = BusinessType.DELETE)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(loginInfoService.deleteLogininforByIds(ids));
    }

    /**
     *
     * 清除系统访问记录
     */
    @OperLog(title = "访问日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("/clean")
    public R clean() {
        loginInfoService.cleanLogininfor();
        return R.ok();
    }

}
