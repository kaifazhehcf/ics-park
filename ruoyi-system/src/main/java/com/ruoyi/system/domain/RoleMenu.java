package com.ruoyi.system.domain;

import lombok.Data;

import javax.persistence.Table;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author ruoyi
 */
@Data
@Table(name = "sys_role_menu")
public class RoleMenu {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
