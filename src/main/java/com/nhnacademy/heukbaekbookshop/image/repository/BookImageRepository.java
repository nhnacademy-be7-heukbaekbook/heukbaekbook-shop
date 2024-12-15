package com.nhnacademy.heukbaekbookshop.image.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

//    Optional<BookImage> findByBookIdAndType(Long book_id, ImageType type);

}
