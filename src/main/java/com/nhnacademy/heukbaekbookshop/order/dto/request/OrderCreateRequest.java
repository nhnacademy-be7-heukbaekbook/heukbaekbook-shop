package com.nhnacademy.heukbaekbookshop.order.dto.request;

import java.util.List;

public record OrderCreateRequest(String totalPrice,
                                 String customerEmail,
                                 String customerName,
                                 String customerPhoneNumber,
                                 String tossOrderId,
                                 List<OrderBookRequest> orderBookRequests) {
}
