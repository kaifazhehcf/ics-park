package com.ruoyi.controller.mobile;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.form.LoginForm;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.IAccessTokenService;
import com.ruoyi.system.service.ISysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录
 *
 * @author jack
 */
@RestController
@RequestMapping("/auth")
public class LoginAPIController {

    @Autowired
    private IAccessTokenService tokenService;
    @Autowired
    private ISysLoginService sysLoginService;

    @PostMapping("login")
    public R login(@RequestBody LoginForm form) {
        // 用户登录
        User user = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(user));
    }

}
