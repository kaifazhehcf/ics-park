package com.ruoyi.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.config.DfsConfig;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.oss.CloudConstant;
import com.ruoyi.system.service.IConfigService;
import com.ruoyi.system.service.IMenuService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 用户 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("system/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IConfigService configService;


    @GetMapping("userType")
    public R type() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, User.UserType> enumMap = EnumUtil.getEnumMap(User.UserType.class);
        for (Map.Entry<String, User.UserType> entry : enumMap.entrySet()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", entry.getKey());
            map.put("text", entry.getValue().getName());
            mapList.add(map);
        }
        return R.data(mapList);
    }

    /**
     * 查询用户
     */
    @GetMapping("get/{userId}")
    public User get(@PathVariable("userId") Long userId) {
        return userService.selectUserById(userId);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("info")
    public User info(@LoginUser User user) {
        String jsonConfig = configService.selectConfigByKey("sys.setting.siteUrl");
        JSONObject jsonObject = JSON.parseObject(jsonConfig);
        String siteUrl = jsonObject.getString("siteUrl");
        user.setAvatar(siteUrl + user.getAvatar());
        user.setButtons(menuService.selectPermsByUserId(user.getUserId()));
        return user;
    }

    /**
     * 查询用户
     */
    @GetMapping("find/{username}")
    public User findByUsername(@PathVariable("username") String username) {
        return userService.selectUserByLoginName(username);
    }

    /**
     * 查询拥有当前角色的所有用户
     */
    @GetMapping("hasRoles")
    public Set<Long> hasRoles(String roleIds) {
        Long[] arr = Convert.toLongArray(roleIds);
        return userService.selectUserIdsHasRoles(arr);
    }

    /**
     * 查询所有当前部门中的用户
     */
    @GetMapping("inDepts")
    public Set<Long> inDept(String deptIds) {
        Long[] arr = Convert.toLongArray(deptIds);
        return userService.selectUserIdsInDepts(arr);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("list")
    public R list(User user) {
        startPage();
        return result(userService.selectUserList(user));
    }


    /**
     * 新增保存用户
     */
    @HasPermissions("system:user:add")
    @PostMapping("save")
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    public R addSave(@RequestBody User user) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        } else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkMobileUnique(user))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        user.setAvatar(Constants.RESOURCE_PREFIX + "/avatar2.jpg");
        user.setCreateBy(getLoginName());
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改保存用户
     */
    @HasPermissions("system:user:edit")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R editSave(@RequestBody User user) {
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkMobileUnique(user))) {
            return R.error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return R.error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getLoginName());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("update/info")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public R updateInfo(@RequestBody User user) {
        return toAjax(userService.updateUserInfo(user));
    }

    /**
     * 记录登陆信息
     *
     * @param user
     * @return
     * @author zmr
     */
    @PostMapping("update/login")
    public R updateLoginRecord(@RequestBody User user) {
        return toAjax(userService.updateUser(user));
    }

    @HasPermissions("system:user:resetPwd")
    @OperLog(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public R resetPwdSave(@RequestBody User user) {
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return toAjax(userService.resetUserPwd(user));
    }

    /**
     * 修改状态
     *
     * @param user
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("status")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public R status(@RequestBody User user) {
        return toAjax(userService.changeStatus(user));
    }

    /**
     * 删除用户
     *
     * @throws Exception
     */
    @HasPermissions("system:user:remove")
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public R remove(String ids) throws Exception {
        return toAjax(userService.deleteUserByIds(ids));
    }

}
