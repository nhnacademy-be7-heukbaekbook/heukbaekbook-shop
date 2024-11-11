package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;
    private final BookFormatter bookFormatter;

    @Override
    public Page<BookResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest) {
        Page<BookDocument> bookDocuments = bookSearchRepository.search(
                pageable,
                searchRequest.keyword(),
                toSearchCondition(searchRequest.searchCondition()),
                toSortCondition(searchRequest.sortCondition())
        );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        return bookDocuments.map(document -> new BookResponse(
                document.getId(),
                document.getTitle(),
                dateFormat.format(document.getPublishedAt()),      // 날짜를 문자열로 변환하여 사용
                document.getSalePrice(),
                document.getDiscountRate(),
                document.getThumbnailUrl(),// 새로 추가된 thumbnailUrl 사용
                document.getContributors(),         // 기여자 목록을 `ContributorSummaryResponse`로 변환하는 메서드 호출
                document.getPublisher() // 출판사 정보를 `PublisherSummaryResponse`로 변환
        ));
    }
    @Scheduled(initialDelay = 0, fixedDelay = 30 * 1000)
    @Transactional
    public void updateBookIndex() {
        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()) {
            System.out.println("No books found in the database.");
            return;
        }

        List<BookDocument> bookDocuments = allBooks.stream()
                .map(this::bookToBookDocument)
                .collect(Collectors.toList());

        bookDocumentRepository.saveAll(bookDocuments);

    }


    private BookDocument bookToBookDocument(Book book) {

        return new BookDocument(
                book.getId(),
                book.getTitle(),
                book.getPublication(),
                bookFormatter.formatPrice(getSalePrice(book.getPrice(), book.getDiscountRate())),
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
    public static SearchCondition toSearchCondition(String condition) {
        try {
            return SearchCondition.valueOf(condition.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return SearchCondition.NONE;
        }
    }

    public static SortCondition toSortCondition(String condition) {
        try {
            return SortCondition.valueOf(condition.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return SortCondition.NONE;
        }
    }

    private BigDecimal getSalePrice(BigDecimal price, double disCountRate) {
        BigDecimal discountRate = BigDecimal.valueOf(disCountRate).divide(BigDecimal.valueOf(100));
        return price.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }

}
