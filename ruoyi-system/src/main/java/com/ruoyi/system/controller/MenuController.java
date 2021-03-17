package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.Menu;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    /**
     * 查询菜单权限
     */
    @GetMapping("get/{menuId}")
    public Menu get(@PathVariable("menuId") Long menuId) {
        return menuService.selectMenuById(menuId);
    }

    @GetMapping("perms/{userId}")
    public Set<String> perms(@PathVariable("userId") Long userId) {
        return menuService.selectPermsByUserId(userId);
    }

    /**
     * 查询菜单权限
     */
    @GetMapping("user")
    public List<Menu> user(@LoginUser User user) {
        return menuService.selectMenusByUser(user);
    }

    /**
     * 根据角色编号查询菜单编号（用于勾选）
     *
     * @param roleId
     * @return
     * @author zmr
     */
    @GetMapping("role/{roleId}")
    public List<Menu> role(@PathVariable("roleId") Long roleId) {
        if (null == roleId || roleId <= 0) {
            return null;
        }
        return menuService.selectMenuIdsByRoleId(roleId);
    }

    /**
     * 查询菜单权限列表
     */
    @GetMapping("list")
    public R list(Menu menu) {
        return result(menuService.selectMenuList(menu));
    }

    /**
     * 查询所有菜单权限列表
     */
    @HasPermissions("system:menu:list")
    @GetMapping("all")
    public R all(Menu menu) {
        return result(menuService.selectMenuList(menu));
    }

    /**
     * 新增保存菜单权限
     */
    @HasPermissions("system:menu:add")
    @PostMapping("save")
    @OperLog(title = "菜单管理", businessType = BusinessType.INSERT)
    public R addSave(@RequestBody Menu menu) {
        menu.setParkId(getParkId());
        menu.setCreateBy(getLoginName());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改保存菜单权限
     */
    @HasPermissions("system:menu:edit")
    @OperLog(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R editSave(@RequestBody Menu menu) {
        menu.setParkId(getParkId());
        menu.setUpdateBy(getLoginName());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单权限
     */
    @OperLog(title = "菜单管理", businessType = BusinessType.DELETE)
    @PostMapping("remove/{menuId}")
    public R remove(@PathVariable("menuId") Long menuId) {
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
