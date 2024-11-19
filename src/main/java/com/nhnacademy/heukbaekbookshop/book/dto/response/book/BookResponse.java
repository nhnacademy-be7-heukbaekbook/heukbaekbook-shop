package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

public record BookResponse(Long id,
                           String title,
                           String publishedAt,
                           String salePrice,
                           double discountRate,
                           String thumbnailUrl,
                           List<ContributorSummaryResponse> contributors,
                           PublisherSummaryResponse publisher) {
}
