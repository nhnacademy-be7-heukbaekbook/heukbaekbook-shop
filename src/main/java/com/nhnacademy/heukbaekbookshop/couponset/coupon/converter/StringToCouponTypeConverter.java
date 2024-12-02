package com.nhnacademy.heukbaekbookshop.couponset.coupon.converter;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import org.springframework.core.convert.converter.Converter;

public class StringToCouponTypeConverter implements Converter<String, CouponType> {
    @Override
    public CouponType convert(String source) {
        try{
            return CouponType.valueOf(source.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid coupon type: " + source);
        }
    }
}
