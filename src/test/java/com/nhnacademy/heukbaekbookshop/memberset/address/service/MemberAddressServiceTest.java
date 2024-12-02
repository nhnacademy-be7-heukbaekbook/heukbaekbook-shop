package com.nhnacademy.heukbaekbookshop.memberset.address.service;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.AddressLimitExceededException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.address.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.impl.MemberAddressServiceImpl;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAddressServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberAddressRepository memberAddressRepository;
    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;

    private final Long testMemberAddressId = 1L;
    private final Long testCustomerId = 1L;
    private final Member testMember = mock(Member.class);
    private final Long testPostalCode = 11111L;
    private final String testRoadNameAddress = "testRoadNameAddress";
    private final String testDetailAddress = "testDetailAddress";
    private final String testAlias = "testAlias";

    @Test
    @DisplayName("회원 주소 조회 실패")
    void getMemberAddress_MemberAddressNotFound_ExceptionThrown() {
        // given
        when(memberAddressRepository.findById(testMemberAddressId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberAddressNotFoundException.class, () -> memberAddressService.getMemberAddress(testMemberAddressId));
        verify(memberAddressRepository, times(1)).findById(testMemberAddressId);
    }

    @Test
    @DisplayName("회원 주소 리스트 조회 성공")
    void getMemberAddressesList_Success() {
        // given
        Long testCustomerId = 1L;
        List<MemberAddress> mockedMemberAddressList = List.of(
                mock(MemberAddress.class), mock(MemberAddress.class));

        when(memberAddressRepository.getAllByMemberIdOrderByCreatedAtDesc(testCustomerId)).thenReturn(mockedMemberAddressList);

        // when
        List<MemberAddressResponse> result = memberAddressService.getMemberAddressesList(testCustomerId);

        // then
        assertThat(result).hasSize(2);
        verify(memberAddressRepository, times(1)).getAllByMemberIdOrderByCreatedAtDesc(testCustomerId);
    }

    @Test
    @DisplayName("회원 주소 생성 시 회원 조회 실패")
    void createMemberAddress_MemberNotFound_ExceptionThrown() {
        // given
        MemberAddressRequest memberAddressRequest = mock(MemberAddressRequest.class);
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberAddressService.createMemberAddress(testMemberAddressId, memberAddressRequest));
        verify(memberRepository, times(1)).findById(testMemberAddressId);
    }

    @Test
    @DisplayName("회원 주소 생성 시 저장 최대 개수 초과 시 실패")
    void createMemberAddress_AddressLimitExceeded_ExceptionThrown() {
        // given
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.of(mock(Member.class)));
        when(memberAddressRepository.countByMemberId(testCustomerId)).thenReturn(10L);

        // when & then
        assertThrows(AddressLimitExceededException.class, () -> memberAddressService.createMemberAddress(testMemberAddressId, mock(MemberAddressRequest.class)));
        verify(memberAddressRepository, times(1)).countByMemberId(testCustomerId);
    }

    @Test
    @DisplayName("회원 주소 생성 시 중복 주소")
    void createMemberAddress_AddressAlreadyExists_ExceptionThrown() {
        // given
        MemberAddressRequest testMemberAddressRequest = new MemberAddressRequest(testPostalCode, testRoadNameAddress, testDetailAddress, testAlias);
        when(memberRepository.findById(testCustomerId)).thenReturn(Optional.of(testMember));
        when(memberAddressRepository.countByMemberId(testCustomerId)).thenReturn(1L);
        when(memberAddressRepository.existsByMemberAndPostalCodeAndDetailAddress(any(),any(), any())).thenReturn(true);

        // when & then
        assertThrows(MemberAddressAlreadyExistsException.class, () -> memberAddressService.createMemberAddress(testCustomerId, testMemberAddressRequest));
        verify(memberAddressRepository, times(1)).existsByMemberAndPostalCodeAndDetailAddress(testMember,testPostalCode, testDetailAddress);
    }

    @Test
    @DisplayName("회원 주소 생성 성공")
    void createMemberAddress_CreateNewMemberAddress_Success() {
        // given
        MemberAddressRequest memberAddressRequest = new MemberAddressRequest(testPostalCode, testRoadNameAddress, testDetailAddress, testAlias);
        MemberAddress testMemberAddress = new MemberAddress(testMember, testPostalCode, testRoadNameAddress, testDetailAddress, testAlias);

        when(memberRepository.findById(any())).thenReturn(Optional.of(testMember));
        when(memberAddressRepository.countByMemberId(any())).thenReturn(1L);
        when(memberAddressRepository.existsByMemberAndPostalCodeAndDetailAddress(any(),any(), any())).thenReturn(false);
        when(memberAddressRepository.save(any(MemberAddress.class))).thenReturn(testMemberAddress);

        // when
        MemberAddressResponse resultMemberAddressResponse = memberAddressService.createMemberAddress(testCustomerId, memberAddressRequest);

        // then
        verify(memberAddressRepository, times(1)).save(any(MemberAddress.class));
        assertNotNull(resultMemberAddressResponse);
    }

    @Test
    @DisplayName("회원 주소 삭제 시 조회 실패")
    void deleteMemberAddress_MemberAddressNotFound_ExceptionThrown() {
        // given & when
        when(memberAddressRepository.existsById(testMemberAddressId)).thenReturn(false);
        // then
        assertThrows(MemberAddressNotFoundException.class, () -> memberAddressService.deleteMemberAddress(testMemberAddressId));
        verify(memberAddressRepository, times(1)).existsById(any());
    }

    @Test
    @DisplayName("회원 주소 삭제 성공")
    void deleteMemberAddress_DeleteMemberAddress_Success() {
        // given
        when(memberAddressRepository.existsById(testMemberAddressId)).thenReturn(true);
        // when
        memberAddressService.deleteMemberAddress(testMemberAddressId);
        // then
        verify(memberAddressRepository, times(1)).deleteById(testMemberAddressId);
    }

    @Test
    @DisplayName("회원 주소 수정 시 회원 주소 조회 실패")
    void updateMemberAddress_AddressNotFound_ExceptionThrown() {
        // given
        when(memberAddressRepository.findById(testMemberAddressId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberAddressNotFoundException.class, () -> memberAddressService.updateMemberAddress(testMemberAddressId, mock(MemberAddressRequest.class)));
        verify(memberAddressRepository, times(1)).findById(testMemberAddressId);
    }

    @Test
    @DisplayName("회원 주소 수정 성공")
    void updateMemberAddress_UpdateAddress_Success() {
        // given
        MemberAddressRequest testMemberAddressRequest = new MemberAddressRequest(testPostalCode, testRoadNameAddress, testDetailAddress,
                "newAlias");
        MemberAddress testMemberAddress = new MemberAddress(testMember, testPostalCode, testRoadNameAddress, testDetailAddress,
                "oldAlias");
        when(memberAddressRepository.findById(testMemberAddressId)).thenReturn(Optional.of(testMemberAddress));

        // when
        MemberAddressResponse resultMemberAddressResponse = memberAddressService.updateMemberAddress(testMemberAddressId, testMemberAddressRequest);

        // then
        verify(memberAddressRepository, times(1)).findById(testMemberAddressId);
        assertEquals("newAlias", resultMemberAddressResponse.alias());
    }
}