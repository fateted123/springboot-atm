package com.dayuanit.dy9.springboot.atm.springbootatm.config;

import com.dayuanit.dy9.springboot.atm.springbootatm.interceptor.LoginInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AtmWebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInteceptor loginInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInteceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/toLogin")
                .excludePathPatterns("/wx/**")
                .excludePathPatterns("/assets/**");
    }
}
