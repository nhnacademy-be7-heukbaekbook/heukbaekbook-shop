package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record OrderDetailResponse(String customerName,
                                  String deliveryFee,
                                  String paymentPrice,
                                  String paymentTypeName,
                                  String recipient,
                                  Long postalCode,
                                  String roadNameAddress,
                                  String detailAddress,
                                  String totalBookPrice,
                                  String totalBookDiscountPrice,
                                  String totalPrice,
                                  String status,
                                  List<OrderBookResponse> books) {

    public static OrderDetailResponse of(Order order) {
        BigDecimal totalBookPrice = calculateTotalBookPrice(order);
        BigDecimal totalDiscount = calculateTotalDiscount(order);
        BigDecimal totalPriceWithDelivery = totalBookPrice.add(order.getDeliveryFee().getFee());

        return new OrderDetailResponse(
                order.getCustomerName(),
                Formatter.formatPrice(order.getDeliveryFee().getFee()),
                Formatter.formatPrice(order.getPayment().getPrice()),
                order.getPayment().getPaymentType().getName(),
                order.getDelivery().getRecipient(),
                order.getDelivery().getPostalCode(),
                order.getDelivery().getRoadNameAddress(),
                order.getDelivery().getDetailAddress(),
                Formatter.formatPrice(totalBookPrice),
                Formatter.formatPrice(totalDiscount),
                Formatter.formatPrice(totalPriceWithDelivery),
                order.getStatus().getKorean(),
                order.getOrderBooks().stream()
                        .map(OrderBookResponse::of)
                        .collect(Collectors.toList())
        );
    }

    private static BigDecimal calculateTotalBookPrice(Order order) {
        return order.getOrderBooks().stream()
                .map(orderBook -> orderBook.getPrice().multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateTotalDiscount(Order order) {
        return order.getOrderBooks().stream()
                .map(orderBook -> orderBook.getBook().getPrice()
                        .subtract(orderBook.getPrice())
                        .multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
