package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findAllByIdInAndType(List<Long> bookIds, ImageType type);

    Page<Book> findAllByPageable(Pageable pageable);
}
