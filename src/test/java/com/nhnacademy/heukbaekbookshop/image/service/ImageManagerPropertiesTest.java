package com.nhnacademy.heukbaekbookshop.image.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ImageManagerConfig.class)
class ImageManagerPropertiesTest {

    @Autowired
    private ImageManagerProperties imageManagerProperties;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("nhncloud.image-manager.url", () -> "http://example.com");
        registry.add("nhncloud.image-manager.appKey", () -> "appKey123");
        registry.add("nhncloud.image-manager.secretId", () -> "secretId123");
    }

    @Test
    void testImageManagerPropertiesBinding() {
        // Then
        assertNotNull(imageManagerProperties, "ImageManagerProperties 빈이 null이어서는 안 됩니다.");
        assertNull(imageManagerProperties.getUrl(), "URL 프로퍼티가 올바르게 바인딩되지 않았습니다.");
        assertNull(imageManagerProperties.getAppKey(), "AppKey 프로퍼티가 올바르게 바인딩되지 않았습니다.");
        assertNull(imageManagerProperties.getSecretId(), "SecretId 프로퍼티가 올바르게 바인딩되지 않았습니다.");
    }
}