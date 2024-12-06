package com.nhnacademy.heukbaekbookshop.order.dto.response;


import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;

import java.time.LocalDate;

public record OrderSummaryResponse(
        LocalDate createdAt,
        String tossOrderId,
        String title,
        String status,
        String customerName,
        String priceAndQuantity,
        DeliverySummaryResponse delivery
) {
    public static OrderSummaryResponse of(Order order) {
        // 제목 생성 로직
        String title = createOrderTitle(order);

        int size = order.getOrderBooks().size();
        String totalPrice = Formatter.formatPrice(order.getTotalPrice());

        // OrderSummaryResponse 생성
        return new OrderSummaryResponse(
                order.getCreatedAt().toLocalDate(),
                order.getTossOrderId(),
                title,
                order.getStatus().getKorean(),
                order.getCustomerName(),
                totalPrice + "/" + size,
                new DeliverySummaryResponse(order.getDelivery().getRecipient())
        );
    }

    private static String createOrderTitle(Order order) {
        int size = order.getOrderBooks().size() - 1;
        String title = order.getOrderBooks().stream()
                .findFirst()
                .map(orderBook -> orderBook.getBook().getTitle())
                .orElse("제목 없음");
        return size > 0 ? title + " 외 " + size + "종" : title;
    }
}
