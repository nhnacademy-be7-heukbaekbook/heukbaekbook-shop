package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.common.util.Calculator;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;

import java.math.BigDecimal;

public record BookSummaryResponse(
        Long id,
        String title,
        boolean isPackable,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal discountRate,
        String thumbnailUrl
) {
    public static BookSummaryResponse of(Book book) {
        return new BookSummaryResponse(
                book.getId(),
                book.getTitle(),
                book.isPackable(),
                book.getPrice(),
                Calculator.getSalePrice(book.getPrice(), book.getDiscountRate()),
                book.getDiscountRate(),
                book.getBookImages().stream()
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse("no-image")
        );
    }
}
