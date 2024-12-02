package com.nhnacademy.heukbaekbookshop.point.history.event;

public record ReviewEvent(
        Long customerId,
        Long orderId,
        boolean hasPhoto
) {
}
