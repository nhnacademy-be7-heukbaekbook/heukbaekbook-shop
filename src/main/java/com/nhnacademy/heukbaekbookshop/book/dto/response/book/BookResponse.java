package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.common.util.Calculator;
import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record BookResponse(Long id,
                           String title,
                           String publishedAt,
                           String salePrice,
                           BigDecimal discountRate,
                           String thumbnailUrl,
                           List<ContributorSummaryResponse> contributors,
                           PublisherSummaryResponse publisher) {

    public static BookResponse of(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                Formatter.formatDate(book.getPublishedAt()),
                Formatter.formatPrice(Calculator.getSalePrice(book.getPrice(), book.getDiscountRate())),
                book.getDiscountRate(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse("no-image"),
                book.getContributors().stream()
                        .map(bookContributor -> new ContributorSummaryResponse(
                                bookContributor.getContributor().getId(),
                                bookContributor.getContributor().getName()
                        ))
                        .collect(Collectors.toList()),
                new PublisherSummaryResponse(
                        book.getPublisher().getId(),
                        book.getPublisher().getName()
                )
        );
    }
}
