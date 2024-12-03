package com.nhnacademy.heukbaekbookshop.cart.service;

import com.nhnacademy.heukbaekbookshop.cart.dto.CartBookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;

import java.util.List;

public interface CartService {

    void createCart(Long customerId, List<CartCreateRequest> cartCreateRequests);

    List<CartBookSummaryResponse> getCartBooks(Long customerId);
}
