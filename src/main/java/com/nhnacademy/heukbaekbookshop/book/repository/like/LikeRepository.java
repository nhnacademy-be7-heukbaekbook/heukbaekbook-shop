package com.nhnacademy.heukbaekbookshop.book.repository.like;

import com.nhnacademy.heukbaekbookshop.book.domain.Like;
import com.nhnacademy.heukbaekbookshop.book.domain.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    Optional<Like> findByBookIdAndCustomerId(Long bookId, Long customerId);
    List<Like> findByCustomerId(Long customerId);
}
