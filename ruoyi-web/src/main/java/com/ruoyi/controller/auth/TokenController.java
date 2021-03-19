package com.ruoyi.controller.auth;

import cn.hutool.core.lang.Validator;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.service.CaptchaService;
import com.ruoyi.common.core.domain.R;

import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.LoginForm;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.system.service.IAccessTokenService;
import com.ruoyi.system.service.ISysLoginService;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录认证
 *
 * @author jack
 */
@RestController
@RequestMapping("auth")
public class TokenController {
    @Autowired
    private IAccessTokenService tokenService;
    @Autowired
    private ISysLoginService sysLoginService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private ISmsService smsService;

    /**
     * 登录
     * @param form
     * @return
     */
    @PostMapping("login/slide")
    public R loginSilde(@RequestBody LoginForm form) {
        ResponseModel response = captchaService.verification(form.getCaptchaVO());
        if (response.isSuccess()) {
            User user = null;
            // 用户名登录
            if (StringUtils.isNotEmpty(form.getUsername()) && StringUtils.isNotEmpty(form.getPassword())) {
                user = sysLoginService.login(form.getUsername(), form.getPassword());
            } else if (StringUtils.isNotEmpty(form.getMobile()) && StringUtils.isNotEmpty(form.getCaptcha()) && Validator.isMobile(form.getMobile())) {
               // 手机号登录
                user = sysLoginService.loginCaptcha(form.getMobile(), form.getCaptcha());
            } else {
                return R.error("用户与手机不能都为空!");
            }
            // 获取登录token
            return R.ok(tokenService.createToken(user));
        }
        return R.error().put("repCode", response.getRepCode());
    }

    @PostMapping("logout")
    public R logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = tokenService.queryByToken(token);
        if (null != user) {
            sysLoginService.logout(user.getLoginName());
            tokenService.expireToken(user.getUserId());
        }
        return R.ok();
    }

    /**
     * 发送手机短信
     */
    @GetMapping("/sms")
    public R sms(String mobile) {
        String content = RandomUtil.randomInt(4);
        String result = smsService.sendLoginSms(mobile, content);
        return StringUtils.equals("OK", result) ? R.ok("发送成功！测试：" + content) : R.error("发送失败, 原因：" + result);
    }

}
