package com.ruoyi.system.mapper;

import com.ruoyi.common.core.dao.BaseMapper;
import com.ruoyi.system.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUserList(User user);

    /**
     * 根据条件分页查询未已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectAllocatedList(User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUnallocatedList(User user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    User selectUserByLoginName(String userName);

    /**
     * 通过Openid查询用户
     *
     * @param openid
     * @return 用户对象信息
     */
    User selectUserByOpenid(String openid);

    /**
     * 通过手机号码查询用户
     *
     * @param mobile 手机号码
     * @return 用户对象信息
     */
    User selectUserByMobile(String mobile);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    User selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    User selectUserById(Long userId);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserByIds(Long[] ids);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(User user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(User user);

    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 登录名称
     * @return 结果
     */
    int checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码是否唯一
     *
     * @param mobile 手机号码
     * @return 结果
     */
    User checkPhoneUnique(String mobile);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    User checkEmailUnique(String email);

    /**
     * 查询拥有当前角色的所有用户编号
     *
     * @param roleIds 角色编号
     * @return
     * @author zmr
     */
    Set<Long> selectUserIdsHasRoles(Long[] roleIds);

    /**
     * 查询拥有当前角色的所有用户编号
     *
     * @param deptIds 部门编号
     * @return
     * @author zmr
     */
    Set<Long> selectUserIdsInDepts(Long[] deptIds);
}
