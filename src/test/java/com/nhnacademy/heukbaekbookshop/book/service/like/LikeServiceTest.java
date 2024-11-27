package com.nhnacademy.heukbaekbookshop.book.service.like;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.domain.Like;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.like.LikeRepository;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLike_Success() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Mock Book");

        Member mockMember = Member.builder()
                .loginId("mockUser")
                .password("password")
                .birth(java.sql.Date.valueOf("2000-01-01"))
                .name("Mock Member")
                .email("mock@example.com")
                .phoneNumber("123-456-7890")
                .build();

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId)).thenReturn(Optional.empty());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(mockMember));

        // When
        LikeCreateResponse response = likeService.createLike(bookId, customerId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Book liked successfully.");
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(bookRepository, times(1)).findById(bookId);
        verify(memberRepository, times(1)).findById(customerId);
    }

    @Test
    void testCreateLike_AlreadyExists() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId))
                .thenReturn(Optional.of(new Like()));

        // When / Then
        assertThrows(LikeAlreadyExistException.class, () -> likeService.createLike(bookId, customerId));
        verify(likeRepository, times(1)).findByBookIdAndCustomerId(bookId, customerId);
        verifyNoInteractions(bookRepository, memberRepository);
    }

    @Test
    void testCreateLike_BookNotFound() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId)).thenReturn(Optional.empty());
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> likeService.createLike(bookId, customerId));
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoInteractions(memberRepository);
    }

    @Test
    void testCreateLike_MemberNotFound() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Mock Book");

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId)).thenReturn(Optional.empty());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> likeService.createLike(bookId, customerId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(memberRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetLikedBooks() {
        // Given
        Long customerId = 1L;

        Publisher mockPublisher = new Publisher();
        mockPublisher.setId(1L);
        mockPublisher.setName("Publisher Name");

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Book Title");
        mockBook.setIndex("Index 1");
        mockBook.setDescription("Description");
        mockBook.setPublishedAt(java.sql.Date.valueOf("2023-11-24"));
        mockBook.setIsbn("1234567890123");
        mockBook.setPackable(true);
        mockBook.setStock(10);
        mockBook.setPrice(BigDecimal.valueOf(20000));
        mockBook.setDiscountRate(BigDecimal.valueOf(10));
        mockBook.setStatus(BookStatus.IN_STOCK);
        mockBook.setPublisher(mockPublisher); // Publisher 필드 초기화

        Like mockLike = Like.builder()
                .bookId(1L)
                .customerId(customerId)
                .book(mockBook)
                .build();

        when(likeRepository.findByCustomerId(customerId)).thenReturn(List.of(mockLike));

        // When
        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(customerId);

        // Then
        assertThat(likedBooks).isNotNull();
        assertThat(likedBooks).hasSize(1);
        assertThat(likedBooks.get(0).id()).isEqualTo(1L);
        assertThat(likedBooks.get(0).title()).isEqualTo("Book Title");
        assertThat(likedBooks.get(0).publisher()).isEqualTo("Publisher Name"); // Publisher 검증

        verify(likeRepository, times(1)).findByCustomerId(customerId);
    }


    @Test
    void testDeleteLike_Success() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        Book mockBook = new Book();
        mockBook.setId(bookId);

        Like mockLike = Like.builder()
                .bookId(bookId)
                .customerId(customerId)
                .book(mockBook)
                .build();

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId)).thenReturn(Optional.of(mockLike));

        // When
        LikeDeleteResponse response = likeService.deleteLike(bookId, customerId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Book unliked successfully.");
        verify(likeRepository, times(1)).findByBookIdAndCustomerId(bookId, customerId);
        verify(likeRepository, times(1)).delete(mockLike);
    }

    @Test
    void testDeleteLike_NotFound() {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;

        when(likeRepository.findByBookIdAndCustomerId(bookId, customerId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(LikeNotFoundException.class, () -> likeService.deleteLike(bookId, customerId));
        verify(likeRepository, times(1)).findByBookIdAndCustomerId(bookId, customerId);
        verify(likeRepository, never()).delete(any());
    }
}
