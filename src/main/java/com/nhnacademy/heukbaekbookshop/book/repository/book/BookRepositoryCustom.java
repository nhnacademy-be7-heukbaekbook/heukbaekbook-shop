package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom {

    List<Book> findAllByBookSearchCondition(BookSearchCondition bookSearchCondition);

    Page<Book> findAllByPageable(Pageable pageable);

    Page<Book> findAllByCategoryIds(List<Long> categoryIds, Pageable pageable);

    Optional<Book> findByBookId(Long bookId);

    void increasePopularityByBookId(Long bookId);
}
