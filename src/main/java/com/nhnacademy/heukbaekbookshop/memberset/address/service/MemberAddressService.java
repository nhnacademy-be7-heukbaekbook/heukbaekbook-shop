package com.nhnacademy.heukbaekbookshop.memberset.address.service;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;

import java.util.List;

public interface MemberAddressService {
    MemberAddressResponse createMemberAddress(Long customerId, MemberAddressRequest memberAddressRequest);

    MemberAddressResponse getMemberAddress(Long addressId);

    List<MemberAddressResponse> getMemberAddressesList(Long customerId);

    MemberAddressResponse updateMemberAddress(Long addressId, MemberAddressRequest memberAddressRequest);

    void deleteMemberAddress(Long addressId);

    Long countByMemberId(Long customerId);
}
