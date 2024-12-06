package com.nhnacademy.heukbaekbookshop.cart.dto;

public record CartBookSummaryResponse(
        Long bookId,
        int quantity
) {
}
