package com.ruoyi.system.service;

import com.ruoyi.system.domain.LoginInfo;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author ruoyi
 */
public interface ILoginInfoService {
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    void insertLogininfor(LoginInfo logininfor);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<LoginInfo> selectLogininforList(LoginInfo logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    int deleteLogininforByIds(String ids);

    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
