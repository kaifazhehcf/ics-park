package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.log.publish.PublishFactory;
import com.ruoyi.system.service.ISmsService;
import com.ruoyi.system.service.ISysLoginService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录认证
 *
 * @author jack
 */
@Service
public class SysLoginServiceImpl implements ISysLoginService {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtils redis;
    @Autowired
    private ISmsService smsService;

    /**
     * 登录
     */
    @Override
    public User login(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null"));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 查询用户信息
        User user = userService.selectUserByLoginName(username);

        if (user == null) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new UserNotExistsException();
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete"));
            throw new UserDeleteException();
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            PublishFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark()));
            throw new UserBlockedException();
        }

        if (!PasswordUtils.matches(user, password)) {
            throw new UserPasswordNotMatchException();
        }
        PublishFactory.recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user);
        return user;
    }

    /**
     * 验证码登录
     *
     * @param mobile
     * @param captcha
     * @return
     */
    @Override
    public User loginCaptcha(String mobile, String captcha) {
        if (!smsService.verify(mobile, captcha, SmsServiceImpl.SmsType.MEMBER_LOGIN)) {
            throw new CaptchaException();
        }

        // 查询用户信息
        User user = userService.selectUserByMobile(mobile);
        PublishFactory.recordLoginInfo(mobile, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user);
        return user;
    }

    /**
     * 记录登录信息
     */
    @Override
    public void recordLoginInfo(User user) {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUser(user);
    }

    /**
     * 登出
     */
    @Override
    public void logout(String loginName) {
        PublishFactory.recordLoginInfo(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
    }
}