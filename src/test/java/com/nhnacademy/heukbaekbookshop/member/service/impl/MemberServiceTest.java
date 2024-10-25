package com.nhnacademy.heukbaekbookshop.member.service.impl;

import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberAlreadyExistException;
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

import java.util.Optional;

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
    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원가입 시 중복 회원 ID")
    void createMember_loginIdAlreadyExists_ExceptionThrown() {
        // given
        MemberCreateRequest memberCreateRequest = mock(MemberCreateRequest.class);
        when(memberCreateRequest.loginId()).thenReturn("existingLoginId");
        when(memberRepository.existsByLoginId(any())).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(memberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
        verify(customerRepository, never()).existsCustomerByEmail(any());
    }

    @Test
    @DisplayName("회원가입 시 중복 회원 email")
    void createMember_emailAlreadyExists_ExceptionThrown() {
        // given
        MemberCreateRequest memberCreateRequest = mock(MemberCreateRequest.class);
        when(memberCreateRequest.email()).thenReturn("existing@email.com");
        when(memberRepository.existsByEmail(any())).thenReturn(true);

        // when & then
        assertThrows(MemberAlreadyExistException.class, () -> memberService.createMember(memberCreateRequest));
        verify(memberRepository, times(1)).existsByLoginId(any());
        verify(customerRepository, never()).existsCustomerByEmail(any());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void createMember_createsNewMember_Success() {
        // given
        MemberCreateRequest request = mock(MemberCreateRequest.class);
        when(request.loginId()).thenReturn("testLoginId");
        when(request.email()).thenReturn("testEmail");
        when(request.password()).thenReturn("testPassword");

        when(memberRepository.existsByLoginId(any())).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(gradeRepository.findById(any())).thenReturn(Optional.of(new Grade()));

        Member member = mock(Member.class);
        when(request.toMemberEntity(any(), any())).thenReturn(member);
        when(request.toMemberAddressEntity(member)).thenReturn(mock(MemberAddress.class));

        // when
        memberService.createMember(request);

        // then
        verify(customerRepository).save(member);
        verify(memberAddressRepository).save(any(MemberAddress.class));
    }

}
