package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookElasticSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;

    @Override
    public Page<BookElasticSearchResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest) {
        Page<BookDocument> bookDocuments = bookSearchRepository.search(
                pageable,
                searchRequest.keyword(),
                toSearchCondition(searchRequest.searchCondition()),
                toSortCondition(searchRequest.sortCondition())
        );
        return bookDocuments.map(document -> new BookElasticSearchResponse(
                document.getId()
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
        String authorNames = book.getContributors().stream()
                .filter(contributor -> contributor.getRole().getRoleName().equals(ContributorRole.AUTHOR))
                .map(contributor -> contributor.getContributor().getName())
                .collect(Collectors.joining(", "));

        return new BookDocument(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                authorNames,
                book.getIsbn(),
                book.getPublication(),
                book.getPublisher().getName(),
                String.valueOf(book.getPrice()),
                book.getDiscountRate(),
                book.getPopularity(),
                convertBookTagsToList(book.getTags())
               // book.getReviewCount(),
        );
    }

    private List<String> convertBookTagsToList(Set<BookTag> bookTags) {
        return bookTags.stream()
                .map(bookTag -> bookTag.getTag().getName()) // BookTag 객체의 태그 이름 필드를 반환하는 메서드 사용
                .collect(Collectors.toList());
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

}
