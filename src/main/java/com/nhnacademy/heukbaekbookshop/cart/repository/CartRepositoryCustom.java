package com.nhnacademy.heukbaekbookshop.cart.repository;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;

import java.util.List;

public interface CartRepositoryCustom {
    List<Cart> searchByCustomerId(Long customerId);
}
