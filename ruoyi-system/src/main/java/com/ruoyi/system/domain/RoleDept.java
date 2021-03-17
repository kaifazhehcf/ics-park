package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Table;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author ruoyi
 */
@Data
@Table(name = "sys_role_dept")
public class RoleDept {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
