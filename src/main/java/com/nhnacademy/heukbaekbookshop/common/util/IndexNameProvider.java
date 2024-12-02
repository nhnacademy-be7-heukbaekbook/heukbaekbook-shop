package com.nhnacademy.heukbaekbookshop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IndexNameProvider {
    @Value("${spring.elasticsearch.index-name}")
    private String baseIndexName;

    public String resolveIndexName() {
        log.info("baseIndexName: {}", baseIndexName);
        return baseIndexName;
    }
}
