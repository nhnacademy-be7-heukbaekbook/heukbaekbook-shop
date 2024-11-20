package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.*;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;
    private final CategoryRepository categoryRepository;
    private final BookFormatter bookFormatter;

    @Override
    public Page<BookResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest) {
        Page<BookDocument> bookDocuments = bookSearchRepository.search(
                pageable,
                searchRequest.keyword(),
                SearchCondition.valueOf(searchRequest.searchCondition().toUpperCase()),
                SortCondition.valueOf(searchRequest.sortCondition().toUpperCase()),
                searchRequest.categoryId()
        );


        return bookDocuments.map(document -> {
            Book book = bookRepository.findById(document.getId()).orElseThrow(() ->
                    new RuntimeException("Book not found with ID: " + document.getId())
            );

            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    bookFormatter.formatDate(book.getPublishedAt()),
                    bookFormatter.formatPrice(getSalePrice(book.getPrice(), book.getDiscountRate())), // BigDecimal -> String 변환
                    book.getDiscountRate(),
                    book.getBookImages().stream()
                            .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                            .map(Image::getUrl)
                            .findFirst()
                            .orElse("no-image"),
                    book.getContributors().stream()
                            .map(contributor -> new ContributorSummaryResponse(
                                    contributor.getContributor().getId(),
                                    contributor.getContributor().getName()
                            ))
                            .collect(Collectors.toList()),
                    new PublisherSummaryResponse(
                            book.getPublisher().getId(),
                            book.getPublisher().getName()
                    )
            );
        });
    }
    @Scheduled(initialDelay = 0, fixedDelay = 30 * 10000)
    @Transactional
    public void updateBookIndex() {
        List<Book> allBooks = bookRepository.findAllByStatusNot(BookStatus.DELETED);
        List<Book> deletedBooks = bookRepository.findAllByStatus(BookStatus.DELETED);

        if (!deletedBooks.isEmpty()) {
            List<Long> deletedBookIds = deletedBooks.stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            bookDocumentRepository.deleteAllById(deletedBookIds);
        }

        List<BookDocument> bookDocuments = allBooks.stream()
                .map(this::bookToBookDocument)
                .collect(Collectors.toList());

        bookDocumentRepository.saveAll(bookDocuments);
    }

    private BookDocument bookToBookDocument(Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(BookCategory::getCategoryId)
                .collect(Collectors.toSet());

        List<Long> allCategoryIds = categoryRepository.findParentCategoryIdsByCategoryIds(categoryIds);

        return new BookDocument(
                book.getId(),
                book.getTitle(),
                book.getPublishedAt(),
                intgetSalePrice(book.getPrice(), book.getDiscountRate()),
                book.getDiscountRate(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse("no-image"),
                getAuthorNames(book),
                book.getDescription(),
                book.getContributors().stream()
                        .map(bookContributor -> new ContributorSummaryResponse(
                                bookContributor.getContributor().getId(),
                                bookContributor.getContributor().getName()
                        ))
                        .collect(Collectors.toList()),
                new PublisherSummaryResponse(
                        book.getPublisher().getId(),
                        book.getPublisher().getName()
                ),
                book.getPopularity(),
                allCategoryIds
        );
    }

    private BigDecimal getSalePrice(BigDecimal price, double disCountRate) {
        BigDecimal discountRate = BigDecimal.valueOf(disCountRate).divide(BigDecimal.valueOf(100));
        return price.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }

    private int intgetSalePrice(BigDecimal price, double discountRate) {

        BigDecimal discount = price.multiply(BigDecimal.valueOf(1 - discountRate / 100));

        return discount.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public List<String> getAuthorNames(Book book) {
        return book.getContributors().stream()
                .filter(bookContributor -> bookContributor.getRole().getRoleName() == ContributorRole.AUTHOR)
                .map(bookContributor -> bookContributor.getContributor().getName())
                .collect(Collectors.toList());
    }
}
