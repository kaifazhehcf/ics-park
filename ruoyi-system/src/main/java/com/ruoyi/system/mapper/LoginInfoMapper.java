package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface LoginInfoMapper {
    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    void insertLoginInfo(LoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    List<LoginInfo> selectLoginInfoList(LoginInfo loginInfo);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteLoginInfoByIds(String[] ids);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    int cleanLoginInfo();
}
