package com.nhnacademy.heukbaekbookshop.memberset.address.dto.mapper;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberAddressMapper {

    public static MemberAddressResponse createMemberAddressResponse(MemberAddress memberAddress) {
        return new MemberAddressResponse(
                memberAddress.getId(),
                memberAddress.getPostalCode(),
                memberAddress.getRoadNameAddress(),
                memberAddress.getDetailAddress(),
                memberAddress.getAlias());
    }

    public static List<MemberAddressResponse> createMemberAddressResponseList(List<MemberAddress> memberAddressList) {
        return memberAddressList.stream().map(memberAddress -> createMemberAddressResponse(memberAddress)).collect(Collectors.toList());
    }

    public static MemberAddress createMemberAddressEntity(MemberAddressRequest memberAddressRequest, Member member) {
        return MemberAddress.builder()
                .member(member)
                .postalCode(memberAddressRequest.postalCode())
                .roadNameAddress(memberAddressRequest.roadNameAddress())
                .detailAddress(memberAddressRequest.detailAddress())
                .alias(memberAddressRequest.alias())
                .build();
    }
}
