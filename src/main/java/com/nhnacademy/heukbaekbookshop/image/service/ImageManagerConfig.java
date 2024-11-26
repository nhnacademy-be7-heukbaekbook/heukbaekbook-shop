package com.nhnacademy.heukbaekbookshop.image.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageManagerConfig {

    @Bean
    @ConfigurationProperties(prefix = "cloud.nhn.image-manager")
    public ImageManagerProperties imageManagerProperties() {
        return new ImageManagerProperties();
    }
}
