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

public record BookViewResponse(
        Long id,
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        boolean isPackable,
        String price,
        String salePrice,
        BigDecimal discountRate,
        long popularity,
        String status,
        String thumbnailUrl,
        List<String> detailImageUrls,
        List<ContributorSummaryResponse> contributors,
        PublisherSummaryResponse publisher) {
    public static BookViewResponse of(Book book) {
        return new BookViewResponse(
                book.getId(),
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                Formatter.formatDate(book.getPublishedAt()),
                book.getIsbn(),
                book.isPackable(),
                Formatter.formatPrice(book.getPrice()),
                Formatter.formatPrice(Calculator.getSalePrice(book.getPrice(), book.getDiscountRate())),
                book.getDiscountRate(),
                book.getPopularity(),
                book.getStatus().name(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse("no-image"),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.DETAIL)
                        .map(Image::getUrl)
                        .collect(Collectors.toList()),
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
