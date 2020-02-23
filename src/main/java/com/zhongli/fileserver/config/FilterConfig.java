package com.zhongli.fileserver.config;

import com.zhongli.fileserver.filter.RestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("restFilter");
        registration.setOrder(1);
        return registration;
    }
}
