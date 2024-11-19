package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

public record BookViewResponse(Long id,
                               String title,
                               String index,
                               String description,
                               String publishedAt,
                               String isbn,
                               boolean isPackable,
                               String price,
                               String salePrice,
                               float discountRate,
                               long popularity,
                               String status,
                               String thumbnailUrl,
                               List<String> detailImageUrls,
                               List<ContributorSummaryResponse> contributors,
                               PublisherSummaryResponse publisher) {
}
