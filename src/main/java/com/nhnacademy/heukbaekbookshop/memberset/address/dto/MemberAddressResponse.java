package com.nhnacademy.heukbaekbookshop.memberset.address.dto;

public record MemberAddressResponse(
        Long addressId,

        Long postalCode,

        String roadNameAddress,

        String detailAddress,

        String alias
) {
}