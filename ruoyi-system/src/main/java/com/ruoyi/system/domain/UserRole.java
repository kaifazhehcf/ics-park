package com.ruoyi.system.domain;

import lombok.Data;

import javax.persistence.Table;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author ruoyi
 */
@Data
@Table(name = "sys_user_role")
public class UserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
