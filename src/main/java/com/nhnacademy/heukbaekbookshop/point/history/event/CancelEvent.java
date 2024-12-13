package com.nhnacademy.heukbaekbookshop.point.history.event;

public record CancelEvent(Long customerId, Long orderId) {
}
