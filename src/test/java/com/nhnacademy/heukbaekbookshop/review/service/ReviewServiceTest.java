//package com.nhnacademy.heukbaekbookshop.review.service;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.Book;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.image.service.ImageManagerService;
//import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
//import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
//import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
//import com.nhnacademy.heukbaekbookshop.order.domain.Order;
//import com.nhnacademy.heukbaekbookshop.order.domain.OrderStatus;
//import com.nhnacademy.heukbaekbookshop.order.domain.Review;
//import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
//import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
//import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
//import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
//import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
//import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
//import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
//import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
//import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class ReviewServiceTest {
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private ReviewImageRepository reviewImageRepository;
//
//    @Mock
//    private ImageManagerService imageManagerService;
//
//    @Mock
//    private PointHistoryRepository pointHistoryRepository;
//
//    @Mock
//    private PointSaveService pointSaveService;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private OrderBookRepository orderBookRepository;
//
//    private ReviewService reviewService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        reviewService = new ReviewService(
//                reviewRepository,
//                reviewImageRepository,
//                orderRepository,
//                imageManagerService,
//                customerRepository,
//                bookRepository,
//                pointSaveService,
//                pointHistoryRepository
//        );
//    }
//
//    @Test
//    void testCreateReview_Success() {
//        // Given
//        Long customerId = 1L;
//        Long orderId = 1L;
//        ReviewCreateRequest request = new ReviewCreateRequest(
//                orderId, 1L, "Content", "Title", 5, List.of(mock(MultipartFile.class))
//        );
//
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mock(Customer.class)));
//
//        Order mockOrder = mock(Order.class);
//        when(mockOrder.getStatus()).thenReturn(OrderStatus.DELIVERED);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
//
//        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
//
//        when(imageManagerService.uploadPhoto(any(MultipartFile.class), any(ImageType.class)))
//                .thenReturn("http://example.com/image.jpg");
//        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());
//
//        // When
//        Review result = reviewService.createReview(customerId, request);
//
//        // Then
//        assertNotNull(result);
//        verify(reviewRepository, times(1)).save(any(Review.class));
//    }
//
//    @Test
//    void testUpdateReview_Success() {
//        // Given
//        Long customerId = 1L;
//        Long orderId = 1L;
//        Long bookId = 1L;
//
//        ReviewUpdateRequest request = new ReviewUpdateRequest();
//        request.setTitle("Updated Title");
//        request.setContent("Updated Content");
//        request.setScore(4);
//        request.setUploadedImages(List.of(mock(MultipartFile.class)));
//
//        Review mockReview = new Review();
//        mockReview.setCustomerId(customerId);
//        mockReview.setBookId(bookId);
//        mockReview.setOrderId(orderId);
//
//        when(reviewRepository.findById(any(ReviewPK.class))).thenReturn(Optional.of(mockReview));
//        when(imageManagerService.uploadPhoto(any(MultipartFile.class), any(ImageType.class)))
//                .thenReturn("http://new_image.jpg");
//
//        // When
//        ReviewDetailResponse response = reviewService.updateReview(customerId, orderId, bookId, request);
//
//        // Then
//        assertEquals("Updated Title", response.title());
//        assertEquals("Updated Content", response.content());
//        verify(imageManagerService, times(1)).uploadPhoto(any(MultipartFile.class), any(ImageType.class));
//        verify(reviewRepository, times(1)).save(any(Review.class));
//    }
//
//    @Test
//    void testSaveReviewPoints_Success() {
//        // Given
//        Long customerId = 1L;
//        Long orderId = 1L;
//        ReviewCreateRequest request = new ReviewCreateRequest(
//                orderId, 1L, "Content", "Title", 5, List.of()
//        );
//
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mock(Customer.class)));
//
//        Order mockOrder = mock(Order.class);
//        when(mockOrder.getStatus()).thenReturn(OrderStatus.DELIVERED);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
//
//        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
//
//        when(pointHistoryRepository.existsByMemberIdAndOrderId(customerId, orderId)).thenReturn(false);
//        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());
//
//        // When
//        reviewService.createReview(customerId, request);
//
//        // Then
//        verify(pointHistoryRepository, times(1)).existsByMemberIdAndOrderId(customerId, orderId);
//        verify(pointSaveService, times(1)).createPointHistory(anyLong(), any(PointHistoryRequest.class));
//    }
//
//
//    @Test
//    void testSaveReviewPoints_AlreadyExists() {
//        // Given
//        Long customerId = 1L;
//        Long orderId = 1L;
//
//        ReviewCreateRequest request = new ReviewCreateRequest(
//                orderId, 1L, "Content", "Title", 5, List.of()
//        );
//
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mock(Customer.class)));
//
//        Order mockOrder = mock(Order.class);
//        when(mockOrder.getStatus()).thenReturn(OrderStatus.DELIVERED);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
//
//        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
//
//        when(pointHistoryRepository.existsByMemberIdAndOrderId(customerId, orderId)).thenReturn(true);
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> reviewService.createReview(customerId, request));
//
//        assertEquals("이미 리뷰 작성으로 포인트가 적립되었습니다.", exception.getMessage());
//        verify(pointHistoryRepository, times(1)).existsByMemberIdAndOrderId(customerId, orderId);
//    }
//
//    @Test
//    void testGetReviewsByBook_Success() {
//        // Given
//        Long bookId = 1L;
//
//        Review mockReview = new Review();
//        mockReview.setBookId(bookId);
//
//        when(reviewRepository.findAllByBookId(anyLong())).thenReturn(List.of(mockReview));
//
//        // When
//        List<ReviewDetailResponse> result = reviewService.getReviewsByBook(bookId);
//
//        // Then
//        assertEquals(1, result.size());
//        assertEquals(bookId, result.get(0).bookId());
//        verify(reviewRepository, times(1)).findAllByBookId(bookId);
//    }
//}
