package com.ruoyi.form;

import com.anji.captcha.model.vo.CaptchaVO;
import lombok.Data;

/**
 * 登录form表单
 *
 * @author jack
 */
@Data
public class LoginForm {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 手机验证码
     */
    private String captcha;

    /**
     * 滑块验证码二次验证参数
     */
    private CaptchaVO captchaVO;
}
