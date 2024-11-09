package com.group1.gosports_jojo.config;

import com.group1.gosports_jojo.filter.UserRedirectFilter;
import com.group1.gosports_jojo.filter.VendorRedirectFilter;
import com.group1.gosports_jojo.filter.AdminRedirectFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<UserRedirectFilter> userFilter() {
        FilterRegistrationBean<UserRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserRedirectFilter());
        registrationBean.addUrlPatterns("/user_profile/*");
        registrationBean.addUrlPatterns("/group_form/*");
        registrationBean.addUrlPatterns("/group_forum_update/*");
        registrationBean.addUrlPatterns("/group_history/*");
        registrationBean.addUrlPatterns("/group_join/*");
        registrationBean.addUrlPatterns("/group_join_close/*");
        registrationBean.setOrder(1);
        System.out.println("會員頁面造訪");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<VendorRedirectFilter> vendorFilter() {
        FilterRegistrationBean<VendorRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new VendorRedirectFilter());
        registrationBean.addUrlPatterns("/vendors/*");
        registrationBean.setOrder(2);
        System.out.println("廠商頁面造訪");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminRedirectFilter> adminFilter() {
        FilterRegistrationBean<AdminRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminRedirectFilter());
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.setOrder(3);
        System.out.println("管理員頁面造訪");
        return registrationBean;
    }
}
