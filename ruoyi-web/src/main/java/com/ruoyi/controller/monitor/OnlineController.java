package com.ruoyi.controller.monitor;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.User;
import com.ruoyi.system.domain.vo.UserOnlineVO;
import com.ruoyi.system.log.annotation.OperLog;
import com.ruoyi.system.log.enums.BusinessType;
import com.ruoyi.system.service.IAccessTokenService;
import com.ruoyi.system.service.ISysLoginService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户记录 提供者
 *
 * @author crud4j
 * @date 2020-09-24
 */
@RestController
@RequestMapping("monitor/online")
public class OnlineController extends BaseController {

    @Autowired
    private IAccessTokenService tokenService;
    @Autowired
    private ISysLoginService sysLoginService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询在线用户记录列表
     */
    @HasPermissions("monitor:online:list")
    @GetMapping("list")
    public R list(String loginName, String ipaddr) {
        List<UserOnlineVO> userOnlineList = Lists.newArrayList();
        Collection<String> keys = redisUtils.keys(Constants.ACCESS_TOKEN + "*");
        for (String key : keys) {
            User user = redisUtils.get(key, User.class);
            // 设置在线用户信息
            if (user.getUserOnline() != null) {
                UserOnlineVO userOnline = user.getUserOnline();
                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(loginName)) {
                    if (StringUtils.equals(ipaddr, userOnline.getIpaddr()) && StringUtils.equals(loginName, userOnline.getLoginName())) {
                        userOnlineList.add(userOnline);
                    }
                } else if (StringUtils.isNotEmpty(ipaddr)) {
                    if (StringUtils.equals(ipaddr, userOnline.getIpaddr())) {
                        userOnlineList.add(userOnline);
                    }
                } else if (StringUtils.isNotEmpty(loginName)) {
                    if (StringUtils.equals(loginName, userOnline.getLoginName())) {
                        userOnlineList.add(userOnline);
                    }
                } else {
                    userOnlineList.add(userOnline);
                }
                Collections.reverse(userOnlineList);
                userOnlineList.removeAll(Collections.singleton(null));
            }
        }
        return result(userOnlineList);
    }


    /**
     * 强退用户
     */
    @HasPermissions("monitor:online:forceLogout")
    @OperLog(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("forceLogout")
    public R forceLogout(String tokenId) {
        User user = tokenService.queryByToken(tokenId);
        if (null != user) {
            sysLoginService.logout(user.getLoginName());
            tokenService.expireToken(user.getUserId());
            return R.ok();
        }
        return R.error("没有找到用户");
    }

}
