package com.nhnacademy.heukbaekbookshop.order.dto.request;

import java.math.BigDecimal;

public record RefundBookRequest(
        Long bookId,
        Integer quantity,
        BigDecimal price
) {}
