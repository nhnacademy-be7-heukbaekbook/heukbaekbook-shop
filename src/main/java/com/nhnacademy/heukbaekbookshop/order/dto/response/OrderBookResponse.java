package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.common.util.Calculator;
import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;

import java.math.BigDecimal;

public record OrderBookResponse(
        Long id,
        String thumbnailUrl,
        String title,
        String price,
        int quantity,
        String salePrice,
        BigDecimal discountRate,
        String totalPrice) {

    public static OrderBookResponse of(OrderBook orderBook) {
        Book book = orderBook.getBook();
        String thumbnailUrl = book.getBookImages().stream()
                .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                .map(Image::getUrl)
                .findFirst()
                .orElse("no-image");

        BigDecimal salePrice = Calculator.getSalePrice(book.getPrice(), book.getDiscountRate());

        return new OrderBookResponse(
                book.getId(),
                thumbnailUrl,
                book.getTitle(),
                Formatter.formatPrice(book.getPrice()),
                orderBook.getQuantity(),
                Formatter.formatPrice(salePrice),
                book.getDiscountRate(),
                Formatter.formatPrice(salePrice.multiply(BigDecimal.valueOf(orderBook.getQuantity())))
        );
    }
}
