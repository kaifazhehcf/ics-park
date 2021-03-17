package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.IEnum;
import com.ruoyi.system.domain.vo.UserOnlineVO;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@Table(name = "sys_user")
public class User extends BaseEntity<User> {
    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    public enum UserType implements IEnum<Integer> {

        /**
         * 会员用户
         */
        MEMBER("会员用户", 0),

        /**
         * 运营用户
         */
        BUSINESS("运营用户", 1),

        /**
         * 平台用户
         */
        ADMIN("平台用户", 2);

        private String name;
        private int value;

        UserType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门父ID
     */
    private Long parentId;

    /**
     * 登录名称
     */
    @NotBlank(message = "登录名称不能为空")
    private String loginName;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    private String userName;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 职位
     */
    private String post;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门对象
     */
    private Dept dept;

    /**
     * 所属公司
     */
    private Long customerId;

    /**
     * 所属园区
     */
    private Long parkId;

    /**
     * 角色信息
     */
    private List<Role> roles;

    /**
     * 角色组
     */
    private List<Long> roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 权限信息
     */
    private Set<String> buttons;

    /**
     * 在线用户记录
     */
    private UserOnlineVO userOnline;


    /**
     * 是否超管
     *
     * @param userId
     * @return
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 纬度
     */
    @Transient
    private BigDecimal lat;

    /**
     * 经度
     */
    @Transient
    private BigDecimal lng;

}
