package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCategoryRepositoryCustom {

    Page<BookCategory> findByCategoryIds(List<Long> categoryIds, Pageable pageable);
}
