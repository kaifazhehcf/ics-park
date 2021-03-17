package com.ruoyi.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.IAccessTokenService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序用户接口
 */
@Slf4j
@RestController
@RequestMapping("/api/wx_auth")
public class WxMaUserController {

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccessTokenService tokenService;

    /**
     * 获取用户信息接口
     */
    @PostMapping("/register")
    public R register(@RequestBody JSONObject inData) {
        try {
            String code = inData.getString("code");
            String signature = inData.getString("signature");
            String rawData = inData.getString("rawData");
            String encryptedData = inData.getString("encryptedData");
            String iv = inData.getString("iv");

            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openId = session.getOpenid();
            // 检查是否是否存在
            User user = userService.selectUserByOpenid(openId);
            // 不存在则注册
            if (user == null) {
                String sessionKey = session.getSessionKey();
                // 用户信息校验
                if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                    return R.error("获取用户信息接口出错");
                }
                // 解密用户信息
                WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                // 设置默认信息
                user = new User();
                user.setUserType(User.UserType.MEMBER);
                user.setLoginName(RandomUtil.randomInt(6));
                user.setUserName(userInfo.getNickName());
                user.setMobile(RandomUtil.randomInt(11));
                user.setGender(userInfo.getGender());
                user.setAvatar(userInfo.getAvatarUrl());
                user.setCreateBy(userInfo.getNickName());
                user.setCreateTime(DateUtils.getNowDate());
                user.setSalt(RandomUtil.randomStr(6));
                user.setOpenid(openId);
                user.setParkId(1L);
                user.setPassword(PasswordUtils.encryptPassword(user.getLoginName(), "admin123", user.getSalt()));
                userService.insertUser(user);
            }
            User pUser = userService.selectUserById(user.getUserId());
            // 获取登录token
            return R.ok(tokenService.createToken(pUser));
        } catch (Exception e) {
            log.error("调用微信服务器出现异常", e);
            return R.error("获取微信用户数据失败");
        }
    }

    /**
     *
     * 获取用户信息接口
     *
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(){

        return R.ok("userInfo");
    }


        /**
         * 获取用户绑定手机号信息
         */
    @PostMapping("/getUserPhone")
    public R getUserPhone(@RequestBody JSONObject inData) {
        try {
            String sessionKey = inData.getString("sessionKey");
            String signature = inData.getString("signature");
            String rawData = inData.getString("rawData");
            String encryptedData = inData.getString("encryptedData");
            String iv = inData.getString("iv");
            // 用户信息校验
            if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return R.error("获取用户信息接口出错");
            }
            // 解密用户信息
            WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
            return R.data(wxMaPhoneNumberInfo);
        } catch (Exception e) {
            log.error("调用微信服务器出现异常", e);
            return R.error("调用微信服务器出现异常");
        }

    }
}