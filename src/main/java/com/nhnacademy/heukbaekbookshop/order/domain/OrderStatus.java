package com.nhnacademy.heukbaekbookshop.order.domain;

public enum OrderStatus {
    WAITING_PAYMENT,
    PAYMENT_COMPLETED,
    PENDING,
    IN_TRANSIT,
    DELIVERED,
    RETURNED,
    CANCELLED
}
