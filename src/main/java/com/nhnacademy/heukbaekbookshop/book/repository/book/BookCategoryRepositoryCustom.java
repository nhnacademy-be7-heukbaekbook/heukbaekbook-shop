package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookCategoryRepositoryCustom {

    Page<BookCategory> findByCategoryIds(List<Long> categoryIds, Pageable pageable);

    Optional<BookCategory> findBookCategoriesByBookId(Long bookId);
}
