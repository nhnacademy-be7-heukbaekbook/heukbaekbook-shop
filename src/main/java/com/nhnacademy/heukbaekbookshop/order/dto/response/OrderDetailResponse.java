package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;
import java.util.List;

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
                                  List<OrderBookResponse> books) {
}
