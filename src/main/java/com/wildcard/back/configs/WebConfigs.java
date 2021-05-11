package com.wildcard.back.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfigs implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathToImage = System.getProperty("user.home") + File.separator + "cardImages" + File.separator;
        registry.addResourceHandler("cardImages/**")
                .addResourceLocations("file:" + pathToImage);
    }
}
