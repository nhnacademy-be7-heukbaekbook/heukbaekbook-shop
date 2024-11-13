package com.nhnacademy.heukbaekbookshop.memberset.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberAddressRequest(
        @NotNull(message = "우편번호를 입력하여 주십시오.")
        Long postalCode,

        @NotBlank(message = "도로명 주소를 입력하여 주십시오.")
        String roadNameAddress,

        @NotBlank(message = "상세 주소를 입력하여 주십시오.")
        @Length(min = 1, max = 20)
        String detailAddress,

        @NotBlank(message = "주소의 별칭을 입력하여 주십시오.")
        @Pattern(regexp = "^[가-힣0-9 ]{1,10}", message = "올바른 주소 별칭 형식이 아닙니다.")
        String alias
) {
}
