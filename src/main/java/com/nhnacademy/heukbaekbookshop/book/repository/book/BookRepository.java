package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Optional<Book> findByIsbn(String isbn);

//    @Query("select new com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse(b.id, b.title, b.price, b.discountRate) " +
//            "from Book b where b.id in :bookIds")
//    List<BookSummaryResponse> findAllByIdIn(@Param("bookIds") List<Long> bookIds);

}

