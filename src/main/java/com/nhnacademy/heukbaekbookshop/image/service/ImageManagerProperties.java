package com.nhnacademy.heukbaekbookshop.image.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "nhncloud.image-manager")
public class ImageManagerProperties {
    private String url;
    private String appKey;
    private String secretId;
}
