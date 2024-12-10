package com.nhnacademy.heukbaekbookshop.memberset.customer.controller;

import com.nhnacademy.heukbaekbookshop.memberset.customer.service.CustomerService;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/orders/{orderId}")
    public OrderDetailResponse getCustomerOrderDetails(@PathVariable String orderId,
                                                       @RequestParam String email) {
        log.info("orderId: {}, email : {}", orderId, email);

        return customerService.getCustomerOrderDetails(orderId, email);
    }
}
