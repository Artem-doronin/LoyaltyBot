package com.example.LoyaltyBot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ChangePasswordInterceptor changePasswordInterceptor;

    public WebConfig(ChangePasswordInterceptor changePasswordInterceptor) {
        this.changePasswordInterceptor = changePasswordInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(changePasswordInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/perform-login",
                        "/logout",
                        "/users/change_password",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/auth/error"
                );

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
