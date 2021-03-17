package com.ruoyi.system.log.listen;

import com.ruoyi.system.domain.LoginInfo;
import com.ruoyi.system.domain.OperLog;
import com.ruoyi.system.log.event.LoginInfoEvent;
import com.ruoyi.system.log.event.OperLogEvent;
import com.ruoyi.system.service.ILoginInfoService;
import com.ruoyi.system.service.IOperLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 *
 * @author ruoyi
 */
@Slf4j
@AllArgsConstructor
public class LogListener {

    @Autowired
    private IOperLogService operLogService;
    @Autowired
    private ILoginInfoService loginInfoService;


    @Async
    @Order
    @EventListener(OperLogEvent.class)
    public void listenOperLog(OperLogEvent event) {
        OperLog operLog = (OperLog) event.getSource();
        operLogService.insertOperlog(operLog);
        log.info("操作日志记录成功：{}", operLog);
    }

    @Async
    @Order
    @EventListener(LoginInfoEvent.class)
    public void listenLoginInfo(LoginInfoEvent event) {
        LoginInfo loginInfo = (LoginInfo) event.getSource();
        loginInfoService.insertLogininfor(loginInfo);
        log.info("访问日志记录成功：{}", loginInfo);
    }

}