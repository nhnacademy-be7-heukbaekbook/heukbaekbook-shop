//package com.nhnacademy.heukbaekbookshop;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.Book;
//import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
//import com.nhnacademy.heukbaekbookshop.book.domain.Like;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
//import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeAlreadyExistException;
//import com.nhnacademy.heukbaekbookshop.book.exception.like.LikeNotFoundException;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.like.LikeRepository;
//import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
//import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
//import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
//import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
//import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test")
//public class LikeServiceTest {
//
//    @Autowired
//    private LikeService likeService;
//
//    @Autowired
//    private LikeRepository likeRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private PublisherRepository publisherRepository;
//
//    private Member testMember;
//    private Book testBook;
//
//    @BeforeEach
//    public void setUp() {
//        // 테스트용 회원 생성 및 저장
//        testMember = Member.builder()
//                .loginId("testuser")
//                .password("password")
//                .name("Test User")
//                .phoneNumber("010-1234-5678")
//                .email("testuser@example.com")
//                .birth(Date.valueOf(LocalDate.of(1990, 1, 1)))
//                .grade(null) // 필요한 경우 등급 설정
//                .build();
//        memberRepository.save(testMember);
//
//        // 테스트용 출판사 생성 및 저장
//        Publisher publisher = new Publisher();
//        publisher.setName("Test Publisher");
//        publisherRepository.save(publisher); // 출판사 저장 추가
//
//        // 테스트용 도서 생성 및 저장
//        testBook = new Book();
//        testBook.setTitle("Test Book");
//        testBook.setIndex("Index");
//        testBook.setDescription("Description");
//        testBook.setPublication(Date.valueOf(LocalDate.of(2023, 1, 1)));
//        testBook.setIsbn("1234567890123");
//        testBook.setPackable(true);
//        testBook.setStock(100);
//        testBook.setPrice(BigDecimal.valueOf(10000));
//        testBook.setDiscountRate(0.1f);
//        testBook.setPopularity(0);
//        testBook.setStatus(BookStatus.IN_STOCK);
//        testBook.setPublisher(publisher);
//        bookRepository.save(testBook); // 도서 저장 추가
//    }
//
//    @Test
//    public void testCreateLike_Success() {
//        // 좋아요 생성 테스트
//        LikeCreateResponse response = likeService.createLike(testBook.getId(), testMember.getId());
//        assertEquals("Book liked successfully.", response.message());
//
//        Optional<Like> likeOptional = likeRepository.findByBookIdAndCustomerId(testBook.getId(), testMember.getId());
//        assertTrue(likeOptional.isPresent());
//    }
//
//    @Test
//    public void testCreateLike_AlreadyExists() {
//        // 이미 좋아요를 누른 경우 예외 발생 테스트
//        likeService.createLike(testBook.getId(), testMember.getId());
//
//        Exception exception = assertThrows(LikeAlreadyExistException.class, () -> {
//            likeService.createLike(testBook.getId(), testMember.getId());
//        });
//
//        assertEquals("이미 좋아요를 누르셨습니다.", exception.getMessage());
//    }
//
//    @Test
//    public void testCreateLike_BookNotFound() {
//        // 존재하지 않는 도서에 좋아요를 누르는 경우 예외 발생 테스트
//        Long nonExistentBookId = 999L;
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            likeService.createLike(nonExistentBookId, testMember.getId());
//        });
//
//        assertEquals("존재하지 않는 책입니다.", exception.getMessage());
//    }
//
//    @Test
//    public void testCreateLike_MemberNotFound() {
//        // 존재하지 않는 회원이 좋아요를 누르는 경우 예외 발생 테스트
//        Long nonExistentMemberId = 999L;
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            likeService.createLike(testBook.getId(), nonExistentMemberId);
//        });
//
//        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
//    }
//
//    @Test
//    public void testGetLikedBooks_Success() {
//        // 좋아요한 도서 목록 조회 성공 테스트
//        likeService.createLike(testBook.getId(), testMember.getId());
//
//        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(testMember.getId());
//
//        assertNotNull(likedBooks);
//        assertEquals(1, likedBooks.size());
//        assertEquals(testBook.getTitle(), likedBooks.get(0).title());
//    }
//
//    @Test
//    public void testGetLikedBooks_NoLikes() {
//        // 좋아요한 도서가 없는 경우 빈 목록 반환 테스트
//        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(testMember.getId());
//
//        assertNotNull(likedBooks);
//        assertTrue(likedBooks.isEmpty());
//    }
//
//    @Test
//    public void testDeleteLike_Success() {
//        // 좋아요 삭제 성공 테스트
//        likeService.createLike(testBook.getId(), testMember.getId());
//
//        LikeDeleteResponse response = likeService.deleteLike(testBook.getId(), testMember.getId());
//        assertEquals("Book unliked successfully.", response.message());
//
//        Optional<Like> likeOptional = likeRepository.findByBookIdAndCustomerId(testBook.getId(), testMember.getId());
//        assertFalse(likeOptional.isPresent());
//    }
//
//    @Test
//    public void testDeleteLike_NotFound() {
//        // 존재하지 않는 좋아요를 삭제하려는 경우 예외 발생 테스트
//        Exception exception = assertThrows(LikeNotFoundException.class, () -> {
//            likeService.deleteLike(testBook.getId(), testMember.getId());
//        });
//
//        assertEquals("존재하지 않는 좋아요입니다.", exception.getMessage());
//    }
//
//    @Test
//    public void testDeleteLike_BookNotFound() {
//        // 존재하지 않는 도서의 좋아요를 삭제하려는 경우 예외 발생 테스트
//        Long nonExistentBookId = 999L;
//
//        Exception exception = assertThrows(LikeNotFoundException.class, () -> {
//            likeService.deleteLike(nonExistentBookId, testMember.getId());
//        });
//
//        assertEquals("존재하지 않는 좋아요입니다.", exception.getMessage());
//    }
//
//    @Test
//    public void testDeleteLike_MemberNotFound() {
//        // 존재하지 않는 회원의 좋아요를 삭제하려는 경우 예외 발생 테스트
//        Long nonExistentMemberId = 999L;
//
//        Exception exception = assertThrows(LikeNotFoundException.class, () -> {
//            likeService.deleteLike(testBook.getId(), nonExistentMemberId);
//        });
//
//        assertEquals("존재하지 않는 좋아요입니다.", exception.getMessage());
//    }
//}
