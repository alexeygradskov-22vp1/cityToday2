package com.example.citytoday.configs.mvc;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/{username}").setViewName("profile");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/news/write");
        registry.addViewController("/news/{news_id}").setViewName("news");
        registry.addViewController("/moderate").setViewName("moderate");
        registry.addViewController("/administrate").setViewName("administrate");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/js/**").
                addResourceLocations("classpath:/static/js/");
        registry.
                addResourceHandler("/css/**").
                addResourceLocations("classpath:/static/css/css/");
        registry.
                addResourceHandler("/images/**").
                addResourceLocations("classpath:/static/images/");
    }
}
