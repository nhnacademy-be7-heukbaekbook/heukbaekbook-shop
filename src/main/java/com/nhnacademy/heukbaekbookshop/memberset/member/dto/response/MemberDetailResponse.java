package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;

import java.math.BigDecimal;
import java.util.List;

public record MemberDetailResponse(Long id,
                                   String customerName,
                                   String customerPhoneNumber,
                                   String customerEmail,
                                   BigDecimal point,
                                   List<MemberAddressResponse> memberAddresses) {
}
