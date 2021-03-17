package com.ruoyi.controller.mobile;

import com.anji.captcha.util.Base64Utils;
import com.google.common.collect.Lists;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Dept;
import com.ruoyi.system.domain.Role;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.log.publish.PublishFactory;
import com.ruoyi.system.service.IAccessTokenService;
import com.ruoyi.system.service.IDeptService;
import com.ruoyi.system.service.IRoleService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;


/**
 * 微信用户直接登录
 *
 * @author jack
 */
@Slf4j
@RestController
@RequestMapping("/wx/login")
public class WxLoginAPIController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IAccessTokenService tokenService;


    @PostMapping
    public R login(@RequestBody User userInfo) {
        try {
            // 检查是否是否存在
            User user = userService.selectUserByOpenid(userInfo.getOpenid());
            // 不存在则注册
            if (user == null) {
                // 设置默认信息
                user = new User();
                String nickname = new String(Base64.getEncoder().encode(userInfo.getUserName().getBytes()), "UTF-8") ;
                // 忽略保存昵称的头像信息
                user.setLoginName(nickname);
                user.setUserName(nickname);
                user.setMobile(RandomUtil.randomInt(11));
                user.setGender(userInfo.getGender());
                user.setAvatar(userInfo.getAvatar());
                user.setCreateBy(nickname);
                user.setCreateTime(DateUtils.getNowDate());
                user.setSalt(RandomUtil.randomStr(6));
                user.setOpenid(userInfo.getOpenid());
                user.setPassword(PasswordUtils.encryptPassword(user.getUserName(), "admin123", user.getSalt()));
                user.setRemark(userInfo.getRemark());
                //默认角色
                Role defaultRole = roleService.findDefault();
                if (StringUtils.isNull(defaultRole)) {
                    return R.error("默认角色未设置，请联系管理员!");
                }
                List<Long> roleIds = Lists.newArrayList();
                roleIds.add(defaultRole.getRoleId());
                user.setRoleIds(roleIds);

                // 默认部门
                Dept defaultDept = deptService.findDefault();
                if (StringUtils.isNull(defaultDept)) {
                    return R.error("默认部门未设置，请联系管理员!");
                }
                user.setDeptId(defaultDept.getDeptId());

                userService.insertUser(user);
            }

            // 登记在线信息
            PublishFactory.recordLoginInfo(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
            return R.ok(tokenService.createToken(user));
        } catch (Exception e) {
            log.error("调用微信服务器出现异常", e);
            return R.error("获取微信用户数据失败");
        }
    }
}
