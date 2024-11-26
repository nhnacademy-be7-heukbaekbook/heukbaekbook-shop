package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;

import java.util.List;

public record MemberDetailResponse(Long id,
                                   String customerName,
                                   String customerPhoneNumber,
                                   String customerEmail,
                                   List<MemberAddressResponse> memberAddresses) {
}
