package com.ruoyi.controller.auth;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ValidatorUtils;
import com.ruoyi.system.domain.Dept;
import com.ruoyi.system.domain.Role;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.IDeptService;
import com.ruoyi.system.service.IRoleService;
import com.ruoyi.system.service.ISmsService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.service.impl.SmsServiceImpl;
import com.ruoyi.system.util.PasswordUtils;
import org.apache.commons.compress.utils.Lists;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户注册
 *
 * @author LeiXuan
 */
@RestController
@RequestMapping("/user/register")
public class RegisterController extends BaseController {

    @Autowired
    private ISmsService smsService;
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtils redis;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IDeptService deptService;

    /**
     * 发送手机短信
     */
    @GetMapping("/sms")
    public R sms(String mobile) {
        User user = userService.selectUserByMobile(mobile);
        if (user != null) {
            return R.error("手机号码已存在");
        }
        String content = RandomUtil.randomInt(4);
        String result = smsService.sendRegisterSms(mobile, content);
        return StringUtils.equals("OK", result) ? R.ok("发送成功！测试：" + content) : R.error("发送失败, 原因：" + result);
    }


    /**
     * 注册提交
     */
    @PostMapping("/submit")
    public R submit(String username, String password, String password2, String captcha, String mobile, Long parkId) {
        if (StringUtils.isEmpty(username)) {
            return R.error("用户名不能为空");
        }
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password2)) {
            return R.error("密码不能为空");
        }
        if (!StringUtils.equals(password, password2)) {
            return R.error("两次密码不一致");
        }
        String key = mobile + SmsServiceImpl.SmsType.MEMBER_REGISTER.getName();
        // 检验
        String pCaptcha = redis.get(key);
        if (StringUtils.isNotEmpty(mobile) && !StringUtils.equals(captcha, pCaptcha)) {
            return R.error("验证码错误");
        }
        if (StringUtils.isEmpty(mobile)) {
            return R.error("手机号码不能为空!");
        }
        if (userService.exists("mobile", mobile)) {
            return R.error("手机号码已存在!");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.error(String.format("新密码长度在%s与%s之间", UserConstants.PASSWORD_MIN_LENGTH, UserConstants.PASSWORD_MAX_LENGTH));
        }
        User user = new User();
        user.setLoginName(username);
        user.setMobile(mobile);
        user.setUserName("用户" + RandomUtil.randomInt(6));
        user.setAvatar("/avatar2.jpg");
        user.setPassword(password);

        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        } else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkMobileUnique(user))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return R.error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }

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

        user.setAvatar(Constants.RESOURCE_PREFIX + "/avatar2.jpg");
        user.setParkId(parkId);
        user.setDeptId(defaultDept.getDeptId());
        user.setUserType(User.UserType.MEMBER);
        user.setCreateBy(user.getMobile());
        user.setCreateTime(DateUtils.getNowDate());
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setRemark("PC-Register");
        return toAjax(userService.insertUser(user));
    }

}
