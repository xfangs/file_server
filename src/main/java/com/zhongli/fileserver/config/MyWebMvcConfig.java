package com.zhongli.fileserver.config;

import com.zhongli.devplatform.vo.ConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = cacheManager.getCache("dev_platform_system_parameter").get("file:baseDir", String::new);
        if (StringUtils.isEmpty(baseDir)) {
            throw new RuntimeException("没有从缓存中获取到系统配置信息,请先启动业务平台");
        }
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + baseDir + "/");
    }

}
