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
import com.nhnacademy.heukbaekbookshop.common.util.Calculator;
import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.common.util.IndexNameProvider;
import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final IndexNameProvider indexNameProvider;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<BookResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest) {
        Page<BookDocument> bookDocuments = bookSearchRepository.search(
                indexNameProvider.resolveIndexName(),
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
                    Formatter.formatDate(book.getPublishedAt()),
                    Formatter.formatPrice(Calculator.getSalePrice(book.getPrice(), book.getDiscountRate())), // BigDecimal -> String 변환
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
        String indexName = indexNameProvider.resolveIndexName();

        // 동적 인덱스 생성 및 매핑 설정
        if (!elasticsearchOperations.indexOps(IndexCoordinates.of(indexName)).exists()) {
            IndexOperations indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
            indexOps.create();
            indexOps.putMapping(elasticsearchOperations.indexOps(BookDocument.class).createMapping());
        }

        List<Book> allBooks = bookRepository.findAllByStatusNot(BookStatus.DELETED);
        List<Book> deletedBooks = bookRepository.findAllByStatus(BookStatus.DELETED);

        // 삭제된 도서 처리
        if (!deletedBooks.isEmpty()) {
            List<Long> deletedBookIds = deletedBooks.stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            deletedBookIds.forEach(bookId ->
                    elasticsearchOperations.delete(String.valueOf(id), IndexCoordinates.of(indexName))
            );
        }

        // 도서 데이터 동적 인덱스에 저장
        List<BookDocument> bookDocuments = allBooks.stream()
                .map(this::bookToBookDocument)
                .collect(Collectors.toList());

        bookDocuments.forEach(bookDocument ->
                elasticsearchOperations.save(bookDocument, IndexCoordinates.of(indexName))
        );
    }

    public BookDocument bookToBookDocument(Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(BookCategory::getCategoryId)
                .collect(Collectors.toSet());

        List<Long> allCategoryIds = categoryRepository.findParentCategoryIdsByCategoryIds(categoryIds);

        List<Review> reviews = reviewRepository.findAllByBookId(book.getId());
        int reviewCount = (reviews != null) ? reviews.size() : 0;
        float reviewScore = 0.0f;
        if (reviews != null && !reviews.isEmpty()) {
            reviewScore = (float) reviews.stream()
                    .mapToDouble(Review::getScore)
                    .average()
                    .orElse(0.0);
        }



        return new BookDocument(
                book.getId(),
                book.getTitle(),
                book.getPublishedAt(),
                intGetSalePrice(book.getPrice(), book.getDiscountRate()),
                book.getDiscountRate().doubleValue(),
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
                allCategoryIds,
                reviewCount,
                reviewScore
        );
    }

    private int intGetSalePrice(BigDecimal price, BigDecimal discountRate) {

        BigDecimal discount = price.multiply(discountRate);

        return discount.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public List<String> getAuthorNames(Book book) {
        return book.getContributors().stream()
                .filter(bookContributor -> bookContributor.getRole().getRoleName() == ContributorRole.AUTHOR)
                .map(bookContributor -> bookContributor.getContributor().getName())
                .collect(Collectors.toList());
    }
}