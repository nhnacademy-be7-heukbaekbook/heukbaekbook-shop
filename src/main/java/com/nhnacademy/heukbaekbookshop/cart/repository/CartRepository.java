package com.nhnacademy.heukbaekbookshop.cart.repository;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
