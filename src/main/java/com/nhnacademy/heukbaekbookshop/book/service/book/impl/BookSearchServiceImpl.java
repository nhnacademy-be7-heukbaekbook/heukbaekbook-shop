package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final BookRepository bookRepository;  // 추가
    private final BookDocumentRepository bookDocumentRepository;  // 추가

        @Override
        public Page<BookSearchResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest) {
        Page<BookDocument> bookDocuments = bookSearchRepository.search(
                pageable,
                searchRequest.getKeyword(),
                searchRequest.getSearchCondition(),
                searchRequest.getSortCondition()
        );

        return bookDocuments.map(document -> BookSearchResponse.builder()
                .title(document.getTitle())
                .description(document.getDescription())
                .author(getAuthorContributors(document))
                .publisher(document.getPublisher().getName())
                .pubDate(convertToLocalDate(document.getPubDate()))
                .salesPrice(calculateSalesPrice(document.getPrice().intValue(),document.getDiscountRate())) // todo
                .build()
        );
    }

    @Scheduled(fixedDelay = 30 * 1000)
    @Transactional
    public void updateBookIndex() {
        List<Book> allBooks = bookRepository.findAll();
        List<BookDocument> bookDocuments = allBooks.stream()
                .map(this::bookToBookDocument)
                .collect(Collectors.toList());
        bookDocumentRepository.saveAll(bookDocuments);
    }

    private BookDocument bookToBookDocument(Book book) {
        return BookDocument.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .contributors(book.getContributors())
                .publisher(book.getPublisher())
                .pubDate(book.getPublication())
                .price(book.getPrice())
                .discountRate(book.getDiscountRate())
                .build();
    }

    private String getAuthorContributors(BookDocument document) {
        return document.getContributors().stream()
                .filter(contributor -> "author".equalsIgnoreCase(contributor.getRole().toString().toLowerCase()))
                .map(contributor -> contributor.getContributor().getName())
                .collect(Collectors.joining(", "));
    }
    public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static int calculateSalesPrice(int standardPrice, float discountRate) {
        // 할인율을 적용한 최종 판매가 계산
        return Math.round(standardPrice * (1 - discountRate));
    }
}


