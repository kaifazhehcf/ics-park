package com.ruoyi.system.log.event;


import com.ruoyi.system.domain.LoginInfo;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 *
 * @author jack
 */
public class LoginInfoEvent extends ApplicationEvent {
    private static final long serialVersionUID = -9084676463718966036L;

    public LoginInfoEvent(LoginInfo source) {
        super(source);
    }
}