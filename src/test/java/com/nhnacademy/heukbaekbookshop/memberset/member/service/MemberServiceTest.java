package com.nhnacademy.heukbaekbookshop.memberset.member.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.OAuthMemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.InvalidPasswordException;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.service.impl.MemberServiceImpl;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.memberset.address.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberAddressRepository memberAddressRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private final Long testCustomerId = 1L;
    private final String testCustomerName = "test";
    private final String testPhoneNumber = "010-0000-0000";
    private final String testEmail = "test@test.com";
    private final String testLoginId = "test";
    private final String testPassword = "test1!";
    private final Date testBirth = mock(Date.class);
    private final Grade testGrade = mock(Grade.class);
    private final Long testPostalCode = 1L;
    private final String testRoadNameAddress = "test";
    private final String testDetailAddress = "test";
    private final String testAlias = "test";


    @Test
    @DisplayName("회원가입 시 중복 회원 ID")
    void createMember_loginIdAlreadyExists_ExceptionThrown() {
        // given
        MemberCreateRequest mockedMemberCreateRequest = mock(MemberCreateRequest.class);
        when(mockedMemberCreateRequest.loginId()).thenReturn(testLoginId);
        when(memberRepository.existsByLoginId(testLoginId)).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(mockedMemberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
    }

    @Test
    @DisplayName("회원가입 시 중복 회원 email")
    void createMember_emailAlreadyExists_ExceptionThrown() {
        // given
        MemberCreateRequest mockedMemberCreateRequest = mock(MemberCreateRequest.class);
        when(mockedMemberCreateRequest.email()).thenReturn(testEmail);
        when(memberRepository.existsByEmail(testEmail)).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(mockedMemberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void createMember_createsNewMember_Success() {
        // given
        MemberCreateRequest testMemberCreateRequest = new MemberCreateRequest(
                testLoginId,
                testPassword,
                testBirth,
                testCustomerName,
                testPhoneNumber,
                testEmail,
                testPostalCode,
                testRoadNameAddress,
                testDetailAddress,
                testAlias);

        when(memberRepository.existsByLoginId(any())).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(gradeRepository.findById(any())).thenReturn(Optional.of(testGrade));

        // when
        MemberResponse testMemberResponse = memberService.createMember(testMemberCreateRequest);

        // then
        verify(customerRepository).save(any(Member.class));
        verify(memberAddressRepository).save(any(MemberAddress.class));
        assertNotNull(testMemberResponse);
    }

    @Test
    void createOAuthMember() {
        //given
        OAuthMemberCreateRequest oAuthMemberCreateRequest = new OAuthMemberCreateRequest(
                testLoginId,
                testPassword,
                testBirth,
                testCustomerName,
                testPhoneNumber,
                testEmail
        );

        when(memberRepository.existsByLoginId(testLoginId)).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(gradeRepository.findById(any())).thenReturn(Optional.of(testGrade));

        //when
        MemberResponse memberResponse = memberService.createOAuthMember(oAuthMemberCreateRequest);

        //then
        assertThat(memberResponse).isNotNull();
        verify(memberRepository, times(1)).existsByLoginId(testLoginId);
        verify(memberRepository, times(1)).existsByEmail(any());
        verify(gradeRepository, times(1)).findById(any());

    }

    @Test
    @DisplayName("회원 조회 실패")
    void getMember_MemberNotFound_ExceptionThrown() {
        // given
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(testCustomerId));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원정보 수정 시 회원 조회 실패")
    void updateMember_MemberNotFound_ExceptionThrown() {
        // given
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(testCustomerId, mock(MemberUpdateRequest.class)));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }


    @Test
    @DisplayName("회원정보 수정 시 비밀번호 불일치")
    void updateMember_InvalidPassword_ExceptionThrown() {
        // given
        Member mockedMember = mock(Member.class);
        when(memberRepository.findById(any())).thenReturn(Optional.of(mockedMember));
        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
                "wrongPassword1!",
                "otherPassword1!",
                testBirth, testCustomerName, testPhoneNumber, testEmail);
        when(mockedMember.getPassword()).thenReturn(testPassword);

        // when & then
        assertThrows(InvalidPasswordException.class, () -> memberService.updateMember(testCustomerId, testMemberUpdateRequest));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원정보 수정 시 기존 비밀번호와 동일")
    void updateMember_SamePassword_ExceptionThrown() {
        // given
        when(memberRepository.findById(any())).thenReturn(Optional.of(mock(Member.class)));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
                "samePassword1!",
                "samePassword1!",
                testBirth, testCustomerName, testPhoneNumber, testEmail);

        // when & then
        assertThrows(InvalidPasswordException.class, () -> memberService.updateMember(testCustomerId, testMemberUpdateRequest));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }


    @Test
    @DisplayName("회원정보 수정 성공")
    void updateMember_UpdateMember_Success() {
        // given
        Member testMember = Member.builder()
                .name("originName")
                .phoneNumber(testPhoneNumber)
                .email(testEmail)
                .loginId(testLoginId)
                .password(bCryptPasswordEncoder.encode("oldPassword1!"))
                .birth(testBirth)
                .grade(testGrade)
                .build();

        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
                "oldPassword1!",
                "newPassword1!",
                testBirth, "changedName", testPhoneNumber, testEmail);

        when(memberRepository.findById(any())).thenReturn(Optional.of(testMember));
        when(bCryptPasswordEncoder.matches(testMemberUpdateRequest.oldPassword(), testMember.getPassword())).thenReturn(true);

        // when
        MemberResponse testMemberResponse = memberService.updateMember(testCustomerId, testMemberUpdateRequest);

        // then
        assertNotNull(testMemberUpdateRequest.oldPassword());
        assertNotNull(testMemberUpdateRequest.newPassword());
        assertNotEquals(testMemberUpdateRequest.oldPassword(), testMemberUpdateRequest.newPassword());
        assertEquals("changedName", testMemberResponse.name());
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원 상태 변경 시 조회 실패")
    void changeMemberStatus_MemberNotFound_ExceptionThrown() {
        // given
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.changeMemberStatus(testCustomerId, mock(MemberStatus.class)));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원 상태 변경 (탈퇴) 성공")
    void changeMemberStatus_Withdrawn_ExceptionThrown() {
        // given
        Member testMember = Member.builder()
                .name(testCustomerName)
                .phoneNumber(testPhoneNumber)
                .email(testEmail)
                .loginId(testLoginId)
                .password("oldPassword1!")
                .birth(testBirth)
                .grade(testGrade)
                .build();

        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.of(testMember));

        // when
        memberService.changeMemberStatus(testCustomerId, MemberStatus.WITHDRAWN);

        // then
        assertEquals(MemberStatus.WITHDRAWN, testMember.getStatus());
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원가입 시 LoginId 중복 확인 조회 성공")
    void existsLoginId_existsLoginIdCheck_Success() {
        when(memberRepository.existsByLoginId(testLoginId)).thenReturn(false);
        assertFalse(memberService.existsLoginId(testLoginId));
    }

    @Test
    @DisplayName("회원가입 시 Email 중복 확인 조회 성공")
    void existsEmail_existsEmailCheck_Success() {
        when(memberRepository.existsByEmail(testEmail)).thenReturn(false);
        assertFalse(memberService.existsEmail(testEmail));
    }

    @Test
    void getMemberDetail() {
        //given
        Member member = Member.builder()
                .name(testCustomerName)
                .phoneNumber(testPhoneNumber)
                .email(testEmail)
                .loginId(testLoginId)
                .password(testPassword)
                .birth(testBirth)
                .grade(testGrade)
                .build();

        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.of(member));
        when(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(testCustomerId)).thenReturn(Optional.empty());

        //when
        MemberDetailResponse memberDetail = memberService.getMemberDetail(testCustomerId);

        //then
        assertThat(memberDetail).isNotNull();
        assertThat(testCustomerName).isEqualTo(memberDetail.customerName());
    }

    @Test
    void getMyPageDetailResponse() {
        //given
        String tossOrderId = "1234";
        Member member = Member.builder()
                .name(testCustomerName)
                .phoneNumber(testPhoneNumber)
                .email(testEmail)
                .loginId(testLoginId)
                .password(testPassword)
                .birth(testBirth)
                .grade(testGrade)
                .build();

        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(tossOrderId, null, null);

        DeliveryFee deliveryFee = new DeliveryFee("기본 배송료", BigDecimal.valueOf(5000), new BigDecimal("30000"));

        Order order = Order.createOrder(
                BigDecimal.valueOf(50000),
                "홍길동",
                "010-1234-5678",
                "wjdehdgus@gmail.com",
                tossOrderId,
                member,
                deliveryFee
        );

        Delivery.createDelivery(
                order,
                "홍길동",
                "010-1234-5678",
                12345L,
                "road",
                "detail",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentType paymentType = new PaymentType(1L, "name");

        Payment payment = new Payment("paymentKey1", order, paymentType, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.valueOf(50000));

        order.setPayment(payment);

        when(memberRepository.searchByCustomerId(testCustomerId)).thenReturn(Optional.of(member));
        when(orderRepository.searchByOrderSearchCondition(orderSearchCondition)).thenReturn(Optional.of(order));


        //when
        MyPageOrderDetailResponse myPageDetailResponse = memberService.getMyPageDetailResponse(testCustomerId, tossOrderId);

        //then
        assertThat(myPageDetailResponse).isNotNull();
        assertThat("홍길동").isEqualTo(order.getCustomerName());
    }
}
