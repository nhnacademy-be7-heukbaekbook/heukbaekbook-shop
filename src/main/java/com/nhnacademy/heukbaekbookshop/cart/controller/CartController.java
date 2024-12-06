package com.nhnacademy.heukbaekbookshop.cart.controller;

import com.nhnacademy.heukbaekbookshop.cart.dto.CartBookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createCart(@RequestHeader(value = "X-USER-ID") Long customerId, @RequestBody List<CartCreateRequest> cartCreateRequests) {
        log.info("customerId: {}, cartCreateRequests : {}", customerId, cartCreateRequests);
        cartService.createCart(customerId, cartCreateRequests);
    }

    @GetMapping
    public List<CartBookSummaryResponse> getCartBooks(@RequestHeader(value = "X-USER-ID") Long customerId) {
        log.info("customerId: {}", customerId);
        return cartService.getCartBooks(customerId);
    }
}
