package com.ruoyi.system.util;

import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.User;

/**
 * 密码加/解密
 * @author jack
 */
public class PasswordUtils {

    public static boolean matches(User user, String newPassword) {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public static String encryptPassword(String username, String password, String salt) {
        return Md5Utils.hash(username + password + salt);
    }
}
