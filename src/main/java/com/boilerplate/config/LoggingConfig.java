package com.boilerplate.config;

import com.boilerplate.common.filter.HttpLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public FilterRegistrationBean<HttpLoggingFilter> httpLoggingFilterRegistration() {
        FilterRegistrationBean<HttpLoggingFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new HttpLoggingFilter());
        reg.addUrlPatterns("/*");
        reg.setOrder(1); // chạy sớm
        return reg;
    }
}
