package com.nhnacademy.heukbaekbookshop.cart.dto;

public record CartCreateRequest(Long bookId, int quantity) {
}
