package com.ruoyi.config;

import com.ruoyi.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 *
 * @author ruoyi
 */
@Configuration
public class FilterConfig {

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public FilterRegistrationBean authFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new DelegatingFilterProxy("authFilter"));
        registration.setName("authFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

}
