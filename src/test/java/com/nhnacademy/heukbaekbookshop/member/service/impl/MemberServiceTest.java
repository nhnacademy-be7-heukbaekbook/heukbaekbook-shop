package com.nhnacademy.heukbaekbookshop.member.service.impl;

import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.exception.InvalidPasswordException;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.member.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        MemberCreateRequest memberCreateRequest = mock(MemberCreateRequest.class);
        when(memberCreateRequest.loginId()).thenReturn(testLoginId);
        when(memberRepository.existsByLoginId(testLoginId)).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(memberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
    }

    @Test
    @DisplayName("회원가입 시 중복 회원 email")
    void createMember_emailAlreadyExists_ExceptionThrown() {
        // given
        MemberCreateRequest memberCreateRequest = mock(MemberCreateRequest.class);
        when(memberCreateRequest.email()).thenReturn(testEmail);
        when(memberRepository.existsByEmail(testEmail)).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(memberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void createMember_createsNewMember_Success() {
        // given
        MemberCreateRequest mockedMemberCreateRequest = mock(MemberCreateRequest.class);

        when(memberRepository.existsByLoginId(any())).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(gradeRepository.findById(any())).thenReturn(Optional.of(testGrade));

        Member member = mock(Member.class);
        when(mockedMemberCreateRequest.toMemberEntity(any(), any())).thenReturn(member);
        when(mockedMemberCreateRequest.toMemberAddressEntity(member)).thenReturn(mock(MemberAddress.class));

        // when
        memberService.createMember(mockedMemberCreateRequest);

        // then
        verify(customerRepository).save(member);
        verify(memberAddressRepository).save(any(MemberAddress.class));
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


//    @Test
//    @DisplayName("회원정보 수정 시 비밀번호 불일치")
//    void updateMember_InvalidPassword_ExceptionThrown() {
//        // given
//        Member mockedMember = mock(Member.class);
//        when(memberRepository.findById(any())).thenReturn(Optional.of(mockedMember));
//        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
//                "wrongPassword1!",
//                "otherPassword1!",
//                testBirth, testCustomerName, testPhoneNumber, testEmail, testPostalCode, testRoadNameAddress, testDetailAddress, testAlias
//        );
//        when(mockedMember.getPassword()).thenReturn(testPassword);
//
//        // when & then
//        assertThrows(InvalidPasswordException.class, () -> memberService.updateMember(testCustomerId, testMemberUpdateRequest));
//        verify(memberRepository, times(1)).findById(testCustomerId);
//    }

    @Test
    @DisplayName("회원정보 수정 시 기존 비밀번호와 동일")
    void updateMember_SamePassword_ExceptionThrown() {
        // given
        when(memberRepository.findById(any())).thenReturn(Optional.of(mock(Member.class)));
        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
                "samePassword1!",
                "samePassword1!",
                testBirth, testCustomerName, testPhoneNumber, testEmail, testPostalCode, testRoadNameAddress, testDetailAddress, testAlias
        );

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
                .password("oldPassword1!")
                .birth(testBirth)
                .grade(testGrade)
                .build();

        MemberUpdateRequest testMemberUpdateRequest = new MemberUpdateRequest(testLoginId,
                "oldPassword1!",
                "newPassword1!",
                testBirth, "changedName", testPhoneNumber, testEmail, testPostalCode, testRoadNameAddress, testDetailAddress, testAlias
        );
        when(memberRepository.findById(any())).thenReturn(Optional.of(testMember));

        // when
        MemberResponse testMemberResponse = memberService.updateMember(testCustomerId, testMemberUpdateRequest);

        // then
        assertEquals("changedName", testMemberResponse.name());
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원 상태 변경 시 조회 실패")
    void changeMemberStatus_MemberNotFound_ExceptionThrown(){
        // given
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.changeMemberStatus(testCustomerId, mock(MemberStatus.class)));
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

    @Test
    @DisplayName("회원 상태 변경 (탈퇴) 성공")
    void changeMemberStatus_Withdrawn_ExceptionThrown(){
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
        MemberResponse testMemberResponse = memberService.changeMemberStatus(testCustomerId, MemberStatus.WITHDRAWN);

        // then
        assertEquals(MemberStatus.WITHDRAWN, testMemberResponse.memberStatus());
        verify(memberRepository, times(1)).findById(testCustomerId);
    }

}
