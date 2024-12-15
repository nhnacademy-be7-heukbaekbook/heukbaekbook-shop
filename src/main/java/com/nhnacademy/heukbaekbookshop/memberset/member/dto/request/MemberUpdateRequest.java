package com.nhnacademy.heukbaekbookshop.memberset.member.dto.request;

import jakarta.validation.constraints.*;

import java.sql.Date;

public record MemberUpdateRequest(
        String loginId,

//        @NotBlank(message = "비밀번호를 입력하여 주십시오.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "올바른 비밀번호 형식이 아닙니다. 비밀번호는 8~16자로 영문 소문자, 숫자, 특수문자가 각각 1개 이상이여야 합니다.")
        String oldPassword,

//        @NotBlank(message = "새 비밀번호를 입력하여 주십시오.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "올바른 비밀번호 형식이 아닙니다. 비밀번호는 8~16자로 영문 소문자, 숫자, 특수문자가 각각 1개 이상이여야 합니다.")
        String newPassword,

        @NotNull(message = "생년월일을 입력하여 주십시오.")
        @PastOrPresent(message = "올바른 생년월일 형식이 아닙니다.")
        Date birth,

        @NotBlank(message = "이름을 입력하여 주십시오.")
        @Pattern(regexp = "^[가-힣]{2,10}", message = "올바른 이름 형식이 아닙니다.")
        String name,

        @NotBlank(message = "전화번호를 입력하여 주십시오.")
        @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 휴대전화 번호 형식이 아닙니다.")
        String phoneNumber,

        @Email
        @NotBlank(message = "이메일을 입력하여 주십시오.")
        @Pattern(regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?", message = "올바른 이메일 형식이 아닙니다.")
        String email

//        @NotNull(message = "우편번호를 입력하여 주십시오.")
//        Long postalCode,
//
//        @NotBlank(message = "도로명 주소를 입력하여 주십시오.")
//        String roadNameAddress,
//
//        @NotBlank(message = "상세 주소를 입력하여 주십시오.")
//        @Length(min = 1, max = 20)
//        String detailAddress,
//
//        @NotBlank(message = "주소의 별칭을 입력하여 주십시오.")
//        @Pattern(regexp = "^[a-zA-Z가-힣0-9 ]{2,10}", message = "올바른 주소 별칭 형식이 아닙니다.")
//        String alias
) { }
