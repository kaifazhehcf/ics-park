package com.ruoyi.controller.mobile;

import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.ISmsService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.service.impl.SmsServiceImpl;
import com.ruoyi.system.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码
 *
 * @author jack
 */
@RestController
@RequestMapping("/password")
public class PasswordAPIController extends BaseController {

    @Autowired
    private ISmsService smsService;
    @Autowired
    private IUserService userService;



    /**
     * 发送手机短信
     */
    @GetMapping("/send_mobile")
    public R sendMobile(String mobile) {
        String content = RandomUtil.randomInt(4);
        String result = smsService.sendForgotPasswordSms(mobile, content);
        return StringUtils.equals("OK", result) ? R.ok("发送成功！测试：" + content) : R.error("发送失败, 原因：" + result);
    }

    /**
     * 忘记密码
     */
    @GetMapping("/forgot")
    public R forgot(String mobile, String code, String password) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            return R.error("手机或验证码不能为空!");
        }

        User user = userService.selectUserByMobile(mobile);
        if (user == null) {
            return R.error("用户不存在!");
        }
        if (!StringUtils.equalsIgnoreCase(user.getMobile(), mobile)) {
            return R.error("用户与手机号不匹配！");
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.error(String.format("新密码长度在%s与%s之间", UserConstants.PASSWORD_MIN_LENGTH, UserConstants.PASSWORD_MAX_LENGTH));
        }

        if (!smsService.verify(mobile, code, SmsServiceImpl.SmsType.FORGOT_PASSWORD)) {
            return R.error("验证码错误或已过期！");
        }

        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), password, user.getSalt()));
        return toAjax(userService.resetUserPwd(user));
    }

    /**
     * 会员重置密码
     */
    @GetMapping("/reset")
    public R resetMember(String password, String newPassword, String reNewPassword, @LoginUser User user) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(reNewPassword)) {
            return R.error("新旧密码不能为空!");
        }
        if (!StringUtils.equals(newPassword, reNewPassword)) {
            return R.error("两次密码录入不一致！");
        }
        // 密码如果不在指定范围内 错误
        if (newPassword.length() < UserConstants.PASSWORD_MIN_LENGTH || newPassword.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.error(String.format("新密码长度在%s与%s之间", UserConstants.PASSWORD_MIN_LENGTH, UserConstants.PASSWORD_MAX_LENGTH));
        }

        if (!PasswordUtils.matches(user, password)) {
            return R.error("旧密码不正确！");
        }

        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
        return toAjax(userService.resetUserPwd(user));
    }

}
