package com.boilerplate.infrastructure.config;

import com.boilerplate.infrastructure.interceptor.HttpLoggingInterceptor;
import com.boilerplate.infrastructure.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final HttpLoggingInterceptor httpLoggingInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLoggingInterceptor)
                .excludePathPatterns("/actuator/health", "/swagger/**", "/v3/api-docs/**",
                        "/static/**", "/webjars/**");
        //redis
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**");
    }


}

