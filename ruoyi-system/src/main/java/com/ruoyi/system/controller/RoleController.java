package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.Role;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/role")
public class RoleController extends BaseController {
    @Autowired
    private IRoleService roleService;

    /**
     * 查询角色
     */
    @GetMapping("get/{roleId}")
    public Role get(@PathVariable("roleId") Long roleId) {
        return roleService.selectRoleById(roleId);
    }

    /**
     * 查询角色列表
     */
    @GetMapping("list")
    public R list(Role role) {
        startPage();
        return result(roleService.selectRoleList(role));
    }

    @GetMapping("all")
    public R all(Role role) {
        if (User.isAdmin(getCurrentUserId())) {
            role.setParkId(null);
        }
        return R.ok().put("rows", roleService.selectRoleAll(role));
    }

    /**
     * 新增保存角色
     */
    @PostMapping("save")
    @OperLog(title = "角色管理", businessType = BusinessType.INSERT)
    public R addSave(@RequestBody Role role) {
        role.setCreateBy(getLoginName());
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R editSave(@RequestBody Role role) {
        role.setUpdateBy(getLoginName());
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("status")
    public R status(@RequestBody Role role) {
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 保存角色分配数据权限
     */
    @HasPermissions("system:role:edit")
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    public R authDataScopeSave(@RequestBody Role role) {
        role.setUpdateBy(getLoginName());
        if (roleService.authDataScope(role) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除角色
     *
     * @throws Exception
     */
    @OperLog(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public R remove(String ids) throws Exception {
        return toAjax(roleService.deleteRoleByIds(ids));
    }
}
