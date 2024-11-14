package com.nhnacademy.heukbaekbookshop.cart.service;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;

import java.util.List;

public interface CartService {

    void createCart(Long customerId, List<CartCreateRequest> cartCreateRequests);
}
