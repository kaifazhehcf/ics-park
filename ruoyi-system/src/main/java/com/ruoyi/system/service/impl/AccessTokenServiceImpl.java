package com.ruoyi.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Maps;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.redis.annotation.RedisEvict;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.AddressUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.domain.vo.UserOnlineVO;
import com.ruoyi.system.service.IAccessTokenService;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Token
 *
 * @author jack
 */
@Service("accessTokenService")
public class AccessTokenServiceImpl implements IAccessTokenService {

    @Autowired
    private RedisUtils redis;

    /**
     * 12小时后过期
     */
    private final static long EXPIRE = 30 * 12 * 60 * 60;

    private final static String ACCESS_TOKEN = Constants.ACCESS_TOKEN;

    private final static String ACCESS_USERID = Constants.ACCESS_USERID;

    @Override
    public User queryByToken(String token) {
        return redis.get(ACCESS_TOKEN + token, User.class);
    }

    @Override
    @RedisEvict(key = "user_perms", fieldKey = "#user.userId")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createToken(User user) {
        // 生成token
        String token = IdUtil.fastSimpleUUID();
        // 记录在线用户
        user = recordUserOnlineInfo(user, token);
        // 保存或更新用户token
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", user.getUserId());
        map.put("token", token);
        map.put("expire", EXPIRE);
        redis.set(ACCESS_TOKEN + token, user, EXPIRE);
        redis.set(ACCESS_USERID + user.getUserId(), token, EXPIRE);
        return map;
    }

    @Override
    public void expireToken(long userId) {
        String token = redis.get(ACCESS_USERID + userId);
        if (StringUtils.isNotBlank(token)) {
            redis.delete(ACCESS_USERID + userId);
            redis.delete(ACCESS_TOKEN + token);
        }
    }

    /**
     * 记录登录信息
     *
     * @param user
     * @param token
     */
    private User recordUserOnlineInfo(final User user, final String token) {
        HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        UserOnlineVO userOnline = new UserOnlineVO();
        userOnline.setTokenId(token);
        userOnline.setLoginName(user.getLoginName());
        userOnline.setLoginTime(DateUtils.getNowDate());
        userOnline.setOs(os);
        userOnline.setIpaddr(ip);
        userOnline.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        userOnline.setBrowser(browser);
        if (user.getDept() != null) {
            userOnline.setDeptName(user.getDept().getDeptName());
        }
        user.setUserOnline(userOnline);
        return user;
    }

}