package com.nhnacademy.heukbaekbookshop.cart.repository;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.domain.CartId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, CartId> {

    List<Cart> findAllByCustomerId(Long customerId);

}
