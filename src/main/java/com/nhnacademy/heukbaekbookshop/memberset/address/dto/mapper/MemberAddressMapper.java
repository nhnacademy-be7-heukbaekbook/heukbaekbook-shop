package com.nhnacademy.heukbaekbookshop.memberset.address.dto.mapper;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberAddressMapper {

    public static MemberAddressDto createMemberAddressResponse(MemberAddress memberAddress) {
        return new MemberAddressDto(
                memberAddress.getPostalCode(),
                memberAddress.getRoadNameAddress(),
                memberAddress.getDetailAddress(),
                memberAddress.getAlias());
    }

    public static List<MemberAddressDto> createMemberAddressResponseList(List<MemberAddress> memberAddressList) {
        return memberAddressList.stream().map(memberAddress -> createMemberAddressResponse(memberAddress)).collect(Collectors.toList());
    }

    public static MemberAddress createMemberAddressEntity(MemberAddressDto memberAddressDto, Member member) {
        return MemberAddress.builder()
                .member(member)
                .postalCode(memberAddressDto.postalCode())
                .roadNameAddress(memberAddressDto.roadNameAddress())
                .detailAddress(memberAddressDto.detailAddress())
                .alias(memberAddressDto.alias())
                .build();
    }
}
