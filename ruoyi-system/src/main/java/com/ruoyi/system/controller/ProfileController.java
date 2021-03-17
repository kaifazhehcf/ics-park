package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.system.config.DfsConfig;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.service.ISmsService;
import com.ruoyi.system.service.IUserService;
import com.ruoyi.system.service.impl.SmsServiceImpl;
import com.ruoyi.system.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息修改
 *
 * @author jack
 */
@RestController
@RequestMapping("system/profile")
public class ProfileController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private DfsConfig dfsConfig;
    @Autowired
    private ISmsService smsService;


    /**
     * 修改头像
     *
     * @param file
     * @param user
     * @return
     */
    @PostMapping("/avatar")
    public R avatar(MultipartFile file, @LoginUser User user) {
        try {
            // 上传文件路径
            String filePath = dfsConfig.getPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = dfsConfig.getDomain() + fileName;
            user.setAvatar(fileName);
            userService.update(user);
            return R.ok().put("fileName", fileName).put("url", url);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 更新当前用户
     */
    @PostMapping("/update")
    public R update(@RequestBody User user, @LoginUser User currentUser) {
        BeanUtils.copyBeanProp(currentUser, user);
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkMobileUnique(currentUser))) {
            return R.error("修改用户'" + currentUser.getLoginName() + "'失败，手机号码已存在");
        }
        currentUser.setUpdateBy(getLoginName());
        return toAjax(userService.updateUser(currentUser));
    }

    /**
     * 会员重置密码
     */
    @GetMapping("/reset")
    public R reset(String password, String newPassword, String rePassword, @LoginUser User user) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(rePassword)) {
            return R.error("新旧密码不能为空!");
        }
        if (!StringUtils.equals(newPassword, rePassword)) {
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

    /**
     * 发送手机短信
     */
    @GetMapping("/sms")
    public R sms(String mobile) {
        String content = RandomUtil.randomInt(4);
        String result = smsService.sendResetSms(mobile, content);
        return StringUtils.equals("OK", result) ? R.ok("发送成功！测试：" + content) : R.error("发送失败, 原因：" + result);
    }

    /**
     * 绑定手机
     */
    @GetMapping("/binding")
    public R binding(String mobile, String captcha, @LoginUser User user) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(captcha)) {
            return R.error("手机或验证码不能为空!");
        }
        if (StringUtils.equals(mobile, user.getMobile())) {
            return R.error("将要修改的手机与系统中一样!");
        }
        if (!smsService.verify(mobile, captcha, SmsServiceImpl.SmsType.RESET_MOBILE)) {
            return R.error("验证码不合法!");
        }
        user.setMobile(mobile);
        return toAjax(userService.updateUser(user));
    }

}
