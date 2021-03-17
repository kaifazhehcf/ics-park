package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 在线用户记录对象
 *
 * @author crud4j
 * @date 2020-09-24
 */
@Data
public class UserOnlineVO {

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用户名称
     */
    private String loginName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Date loginTime;

}
