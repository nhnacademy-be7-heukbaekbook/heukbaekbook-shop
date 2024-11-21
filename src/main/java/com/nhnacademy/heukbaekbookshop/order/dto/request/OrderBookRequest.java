package com.nhnacademy.heukbaekbookshop.order.dto.request;

public record OrderBookRequest(Long bookId,
                               boolean isWrapped,
                               Long wrappingPaperId) {
}

