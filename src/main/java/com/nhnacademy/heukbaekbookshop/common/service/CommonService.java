package com.nhnacademy.heukbaekbookshop.common.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CommonService {

    public BigDecimal convertStringToBigDecimal(String amount) {
        amount = amount.replace(",", "");
        return new BigDecimal(amount);
    }
}
