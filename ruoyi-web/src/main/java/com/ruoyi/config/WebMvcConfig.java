package com.ruoyi.config;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.system.config.DfsConfig;
import com.ruoyi.system.resolver.LoginUserHandlerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MVC配置
 *
 * @author ruoyi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserHandlerResolver loginUserHandlerResolver;

    @Autowired
    private DfsConfig dfsConfig;

    /**
     * 本地文件上传路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + dfsConfig.getPath() + "/");
    }

    /**
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerResolver);
    }

}