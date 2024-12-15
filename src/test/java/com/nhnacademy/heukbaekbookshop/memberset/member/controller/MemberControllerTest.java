package com.nhnacademy.heukbaekbookshop.memberset.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliverySummaryResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService,likeService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testCreateMemberWithValidData() throws Exception {
        // 유효한 MemberCreateRequest 데이터 생성
        MemberCreateRequest validRequest = new MemberCreateRequest(
                "testuser",
                "Password@123",
                new Date(LocalDateTime.now().minusYears(20).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                "홍길동",
                "010-1234-5678",
                "test@example.com",
                12345L,
                "서울특별시 강남구 테헤란로",
                "101호",
                "우리집" // 정규식과 일치하는 별칭
        );

        // Mock MemberResponse 데이터 생성
        MemberResponse mockResponse = new MemberResponse(
                "홍길동",
                "010-1234-5678",
                "test@example.com",
                "testuser",
                new Date(LocalDateTime.now().minusYears(20).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                MemberStatus.ACTIVE,
                new GradeDto("Gold", new BigDecimal("0.1"), new BigDecimal("1000.0"))
        );

        // Mock 서비스 동작 설정
        when(memberService.createMember(any(MemberCreateRequest.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.loginId").value("testuser"))
                .andExpect(jsonPath("$.memberStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.grade.gradeName").value("Gold"))
                .andExpect(jsonPath("$.grade.pointPercentage").value("0.1"))
                .andExpect(jsonPath("$.grade.promotionStandard").value("1000.0"));
    }

    @Test
    void testGetMemberWithValidCustomerId() throws Exception {
        // Mock MemberResponse 데이터 생성
        MemberResponse mockResponse = new MemberResponse(
                "홍길동",
                "010-1234-5678",
                "test@example.com",
                "testuser",
                new Date(LocalDateTime.now().minusYears(20).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                MemberStatus.ACTIVE,
                new GradeDto("Gold", new BigDecimal("0.1"), new BigDecimal("1000.0"))
        );

        // Mock 서비스 동작 설정
        when(memberService.getMember(anyLong())).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(get("/api/members")
                        .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.loginId").value("testuser"))
                .andExpect(jsonPath("$.memberStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.grade.gradeName").value("Gold"))
                .andExpect(jsonPath("$.grade.pointPercentage").value("0.1"))
                .andExpect(jsonPath("$.grade.promotionStandard").value("1000.0"));
    }

    @Test
    void testUpdateMemberWithValidData() throws Exception {
        // 유효한 MemberUpdateRequest 데이터 생성
        MemberUpdateRequest validRequest = new MemberUpdateRequest(
                "testuser",
                "OldPassword@123",
                "NewPassword@123",
                new Date(LocalDateTime.now().minusYears(25).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                "홍길동",
                "010-1234-5678",
                "test@example.com"
        );

        // Mock MemberResponse 데이터 생성
        MemberResponse mockResponse = new MemberResponse(
                "홍길동",
                "010-1234-5678",
                "test@example.com",
                "testuser",
                new Date(LocalDateTime.now().minusYears(25).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                MemberStatus.ACTIVE,
                new GradeDto("Gold", new BigDecimal("0.1"), new BigDecimal("1000.0"))
        );

        // Mock 서비스 동작 설정
        when(memberService.updateMember(anyLong(), any(MemberUpdateRequest.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(put("/api/members")
                        .header(MemberController.X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.loginId").value("testuser"))
                .andExpect(jsonPath("$.memberStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.grade.gradeName").value("Gold"))
                .andExpect(jsonPath("$.grade.pointPercentage").value("0.1"))
                .andExpect(jsonPath("$.grade.promotionStandard").value("1000.0"));
    }

    @Test
    void testDeleteMember() throws Exception {
        // Mock 서비스 동작 설정
        doNothing().when(memberService).changeMemberStatus(anyLong(), eq(MemberStatus.WITHDRAWN));

        // 테스트 수행
        mockMvc.perform(delete("/api/members")
                        .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isNoContent());

        // Verify that the service method was called with the correct parameters
        verify(memberService, times(1)).changeMemberStatus(1L, MemberStatus.WITHDRAWN);
    }

    @Test
    void testGetLikedBooks() throws Exception {
        // Mock BookDetailResponse 리스트 생성
        List<BookDetailResponse> mockLikedBooks = List.of(
                new BookDetailResponse(
                        1L,
                        "Java Programming",
                        "001",
                        "A comprehensive guide to Java programming.",
                        "2023-01-01",
                        "9781234567890",
                        "https://example.com/images/java-programming.jpg",
                        List.of("https://example.com/images/java-detail1.jpg", "https://example.com/images/java-detail2.jpg"),
                        true,
                        10,
                        30000,
                        new BigDecimal("0.15"),
                        "AVAILABLE",
                        "Tech Publishers",
                        List.of("Programming", "Java"),
                        List.of("John Doe"),
                        List.of("Beginner", "Java")
                ),
                new BookDetailResponse(
                        2L,
                        "Spring Framework",
                        "002",
                        "A detailed introduction to Spring Framework.",
                        "2022-06-15",
                        "9780987654321",
                        "https://example.com/images/spring-framework.jpg",
                        List.of("https://example.com/images/spring-detail1.jpg", "https://example.com/images/spring-detail2.jpg"),
                        true,
                        5,
                        45000,
                        new BigDecimal("0.20"),
                        "AVAILABLE",
                        "Spring Publications",
                        List.of("Framework", "Spring"),
                        List.of("Jane Smith"),
                        List.of("Advanced", "Spring")
                )
        );

        // Mock 서비스 동작 설정
        when(likeService.getLikedBooks(anyLong())).thenReturn(mockLikedBooks);

        // 테스트 수행
        mockMvc.perform(get("/api/members/likes")
                        .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Java Programming"))
                .andExpect(jsonPath("$[0].isbn").value("9781234567890"))
                .andExpect(jsonPath("$[0].publisher").value("Tech Publishers"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Spring Framework"))
                .andExpect(jsonPath("$[1].isbn").value("9780987654321"))
                .andExpect(jsonPath("$[1].publisher").value("Spring Publications"));
    }

    @Test
    void testExistsLoginId() throws Exception {
        // Mock 서비스 동작 설정: 해당 loginId가 존재하는 경우
        when(memberService.existsLoginId("testuser")).thenReturn(true);

        // 테스트 수행
        mockMvc.perform(get("/api/members/existsLoginId/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Mock 서비스 동작 설정: 해당 loginId가 존재하지 않는 경우
        when(memberService.existsLoginId("nonexistentuser")).thenReturn(false);

        // 테스트 수행
        mockMvc.perform(get("/api/members/existsLoginId/nonexistentuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testExistsEmail() throws Exception {
        // Mock 서비스 동작 설정: 해당 email이 존재하는 경우
        when(memberService.existsEmail("test@example.com")).thenReturn(true);

        // 테스트 수행
        mockMvc.perform(get("/api/members/existsEmail/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Mock 서비스 동작 설정: 해당 email이 존재하지 않는 경우
        when(memberService.existsEmail("nonexistent@example.com")).thenReturn(false);

        // 테스트 수행
        mockMvc.perform(get("/api/members/existsEmail/nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetMemberDetail() throws Exception {
        // Mock MemberAddressResponse 리스트 생성
        List<MemberAddressResponse> mockAddresses = List.of(
                new MemberAddressResponse(1L, 12345L, "서울특별시 강남구 테헤란로", "101호", "집"),
                new MemberAddressResponse(2L, 67890L, "서울특별시 서초구 반포대로", "202호", "회사")
        );

        // Mock MemberDetailResponse 생성
        MemberDetailResponse mockResponse = new MemberDetailResponse(
                1L,
                "홍길동",
                "010-1234-5678",
                "test@example.com",
                BigDecimal.valueOf(0),
                mockAddresses
        );

        // Mock 서비스 동작 설정
        when(memberService.getMemberDetail(anyLong())).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(get("/api/members/detail")
                        .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("홍길동"))
                .andExpect(jsonPath("$.customerPhoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.customerEmail").value("test@example.com"))
                .andExpect(jsonPath("$.memberAddresses.size()").value(2))
                .andExpect(jsonPath("$.memberAddresses[0].addressId").value(1L))
                .andExpect(jsonPath("$.memberAddresses[0].postalCode").value(12345L))
                .andExpect(jsonPath("$.memberAddresses[0].roadNameAddress").value("서울특별시 강남구 테헤란로"))
                .andExpect(jsonPath("$.memberAddresses[0].detailAddress").value("101호"))
                .andExpect(jsonPath("$.memberAddresses[0].alias").value("집"))
                .andExpect(jsonPath("$.memberAddresses[1].addressId").value(2L))
                .andExpect(jsonPath("$.memberAddresses[1].postalCode").value(67890L))
                .andExpect(jsonPath("$.memberAddresses[1].roadNameAddress").value("서울특별시 서초구 반포대로"))
                .andExpect(jsonPath("$.memberAddresses[1].detailAddress").value("202호"))
                .andExpect(jsonPath("$.memberAddresses[1].alias").value("회사"));
    }

    @Test
    void getMyPageResponse() throws Exception {
        //given
        Long customerId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        GradeDto gradeDto = new GradeDto("골드", BigDecimal.valueOf(10), BigDecimal.valueOf(500));
        DeliverySummaryResponse deliverySummaryResponse = new DeliverySummaryResponse("홍길동");
        OrderSummaryResponse orderSummaryResponse = new OrderSummaryResponse(LocalDateTime.now().toLocalDate(), "1234", "title", "배송중", "홍길동", "15000/1", deliverySummaryResponse);
        List<OrderSummaryResponse> orderSummaryResponses = List.of(orderSummaryResponse);
        Page<OrderSummaryResponse> orderSummaryResponsePage = new PageImpl<>(orderSummaryResponses);
        OrderResponse orderResponse = new OrderResponse(orderSummaryResponsePage);

        MyPageResponse myPageResponse = new MyPageResponse(gradeDto, orderResponse);

        //when
        when(memberService.getMyPageResponse(customerId, pageRequest)).thenReturn(myPageResponse);

        //then
        mockMvc.perform(
                get("/api/members/my-page")
                .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void myPageOrderDetailResponse() throws Exception {
        //given
        Long customerId = 1L;
        String orderId = "1";
        GradeDto gradeDto = new GradeDto("골드", BigDecimal.valueOf(10), BigDecimal.valueOf(500));
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(orderId, "홍길동", "5000", "20000", "toss", "홍길동", 12345L, "test", "test", "30000", "2000", "1000", "28000", "배송중", null);
        MyPageOrderDetailResponse myPageOrderDetailResponse = new MyPageOrderDetailResponse(gradeDto, orderDetailResponse);

        //when
        when(memberService.getMyPageDetailResponse(customerId, orderId)).thenReturn(myPageOrderDetailResponse);

        //then
        mockMvc.perform(
                get("/api/members/orders/1")
                        .header(MemberController.X_USER_ID, 1L))
                .andExpect(status().isOk());
    }
}
