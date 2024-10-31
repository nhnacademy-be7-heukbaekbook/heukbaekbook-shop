package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepositoryCustom {

    List<BookSummaryResponse> findAllByIdIn(@Param("bookIds") List<Long> bookIds);
}
