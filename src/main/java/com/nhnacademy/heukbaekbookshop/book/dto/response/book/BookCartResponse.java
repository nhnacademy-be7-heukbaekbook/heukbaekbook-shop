package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

public record BookCartResponse(Long id, String title, String price, String salePrice, double discountRate, String thumbnailUrl) {
}
