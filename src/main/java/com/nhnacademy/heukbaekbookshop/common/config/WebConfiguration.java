package com.nhnacademy.heukbaekbookshop.common.config;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.converter.StringToCouponTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToCouponTypeConverter());
    }
}
