package com.nhnacademy.heukbaekbookshop.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndexNameProvider {
    @Value("${spring.elasticsearch.index-name}")
    private String baseIndexName;

    public String resolveIndexName() {
        return baseIndexName;
    }
}
