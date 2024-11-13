package com.nhnacademy.heukbaekbookshop.memberset.address.service;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;

import java.util.List;

public interface MemberAddressService {
    MemberAddressDto createMemberAddress(Long customerId, MemberAddressDto memberAddressDto);

    MemberAddressDto getMemberAddress(Long addressId);

    List<MemberAddressDto> getMemberAddressesList(Long customerId);

    MemberAddressDto updateMemberAddress(Long addressId, MemberAddressDto memberAddressDto);

    void deleteMemberAddress(Long addressId);

    Long countByMemberId(Long customerId);
}
