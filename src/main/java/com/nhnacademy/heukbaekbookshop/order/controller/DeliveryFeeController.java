package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.service.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/delivery-fees")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    @GetMapping
    public BigDecimal getDeliveryFeeByMinimumOrderAmount(@RequestParam BigDecimal minimumOrderAmount) {
        log.info("minimumOrderAmount : {}", minimumOrderAmount);
        return deliveryFeeService.getDeliveryFeeByMinimumOrderAmount(minimumOrderAmount);
    }
}
