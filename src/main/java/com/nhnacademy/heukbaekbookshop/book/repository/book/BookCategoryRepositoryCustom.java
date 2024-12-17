package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;

import java.util.Optional;

public interface BookCategoryRepositoryCustom {
    Optional<BookCategory> findBookCategoriesByBookId(Long bookId);
}
