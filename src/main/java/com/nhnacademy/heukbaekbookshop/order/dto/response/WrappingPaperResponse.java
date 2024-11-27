package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;

import java.math.BigDecimal;

public record WrappingPaperResponse(
        Long id,
        String name,
        BigDecimal price,
        String imageUrl
) {
    public static WrappingPaperResponse of(WrappingPaper wrappingPaper) {
        return new WrappingPaperResponse(
                wrappingPaper.getId(),
                wrappingPaper.getName(),
                wrappingPaper.getPrice(),
                wrappingPaper.getWrappingPaperImage().getUrl()
        );
    }
}
