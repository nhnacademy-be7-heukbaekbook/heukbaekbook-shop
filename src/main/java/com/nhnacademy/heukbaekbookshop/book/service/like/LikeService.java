package com.nhnacademy.heukbaekbookshop.book.service.like;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.Like;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.like.LikeRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.likeRepository = likeRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public LikeCreateResponse createLike(Long bookId, Long customerId) {
        if (likeRepository.findByBookIdAndCustomerId(bookId, customerId).isPresent()) {
            throw new LikeAlreadyExistException("이미 좋아요를 누르셨습니다.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Like like = Like.builder()
                .bookId(bookId)
                .customerId(customerId)
                .book(book)
                .member(member)
                .build();

        likeRepository.save(like);
        return new LikeCreateResponse("Book liked successfully.");
    }

    public List<BookDetailResponse> getLikedBooks(Long customerId) {
        List<Like> likes = likeRepository.findByCustomerId(customerId);
        return likes.stream()
                .map(like -> mapToBookDetailResponse(like.getBook()))
                .collect(Collectors.toList());
    }

    public LikeDeleteResponse deleteLike(Long bookId, Long customerId) {
        Like like = likeRepository.findByBookIdAndCustomerId(bookId, customerId)
                .orElseThrow(() -> new LikeNotFoundException("존재하지 않는 좋아요입니다."));
        likeRepository.delete(like);
        return new LikeDeleteResponse("Book unliked successfully.");
    }

    private BookDetailResponse mapToBookDetailResponse(Book book) {
        return new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublishedAt().toString(),
                book.getIsbn(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse(null),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.DETAIL)
                        .map(Image::getUrl)
                        .collect(Collectors.toList()),
                book.isPackable(),
                book.getStock(),
                book.getPrice().intValue(),
                book.getDiscountRate(),
                book.getStatus().toString(),
                book.getPublisher().getName(),
                book.getCategories().stream()
                        .map(bc -> buildCategoryPath(bc.getCategory()))
                        .collect(Collectors.toList()),
                book.getContributors().stream()
                        .filter(bc -> bc.getRole().getRoleName() == ContributorRole.AUTHOR)
                        .map(bc -> bc.getContributor().getName())
                        .collect(Collectors.toList()),
                book.getTags().stream()
                        .map(bt -> bt.getTag().getName())
                        .collect(Collectors.toList())
        );
    }

    private String buildCategoryPath(Category category) {
        List<String> categoryNames = new LinkedList<>();
        while (category != null) {
            categoryNames.add(0, category.getName());
            category = category.getParentCategory();
        }
        return String.join(">", categoryNames);
    }
}
