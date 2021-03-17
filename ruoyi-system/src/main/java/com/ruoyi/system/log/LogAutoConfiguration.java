package com.ruoyi.system.log;

import com.ruoyi.system.log.aspect.OperLogAspect;
import com.ruoyi.system.log.listen.LogListener;
import com.ruoyi.system.service.ILoginInfoService;
import com.ruoyi.system.service.IOperLogService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志配置
 *
 * @author jack
 */
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {

    private final IOperLogService logService;
    private final ILoginInfoService sysLogininforService;

    @Bean
    public LogListener sysOperLogListener() {
        return new LogListener(logService, sysLogininforService);
    }

    @Bean
    public OperLogAspect operLogAspect() {
        return new OperLogAspect();
    }
}