package com.group1.gosports_jojo.config;

import com.group1.gosports_jojo.filter.LoginRedirectFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<LoginRedirectFilter> LoginRedirectFilter(){
        FilterRegistrationBean<LoginRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginRedirectFilter());

        //設定需要攔截的url
        registrationBean.addUrlPatterns("/user_profile/*");
        return registrationBean;
    }
}
