package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findAllByStatusNot(BookStatus status, Pageable pageable);

//    @Query("select new com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse(b.id, b.title, b.price, b.discountRate) " +
//            "from Book b where b.id in :bookIds")
//    List<BookSummaryResponse> findAllByIdIn(@Param("bookIds") List<Long> bookIds);
}