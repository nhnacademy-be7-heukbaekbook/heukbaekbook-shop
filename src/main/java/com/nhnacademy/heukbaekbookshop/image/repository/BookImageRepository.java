package com.nhnacademy.heukbaekbookshop.image.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;

import java.util.Optional;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

//    Optional<BookImage> findByBookIdAndType(Long book_id, ImageType type);

}
