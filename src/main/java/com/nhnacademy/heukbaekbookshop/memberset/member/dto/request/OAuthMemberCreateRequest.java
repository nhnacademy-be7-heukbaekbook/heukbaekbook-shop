package com.nhnacademy.heukbaekbookshop.memberset.member.dto.request;


import jakarta.validation.constraints.*;

import java.sql.Date;

public record OAuthMemberCreateRequest(
        @NotBlank(message = "아이디를 입력하여 주십시오.")
        @Pattern(regexp = "^[a-z0-9-]{3,40}")
        String loginId,

        @NotBlank(message = "비밀번호를 입력하여 주십시오.")
        String password,

        @NotNull(message = "생년월일을 입력하여 주십시오.")
        @PastOrPresent(message = "올바른 생년월일 형식이 아닙니다.")
        Date birth,

        @NotBlank(message = "이름을 입력하여 주십시오.")
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "올바른 이름 형식이 아닙니다.")
        String name,

        @NotBlank(message = "전화번호를 입력하여 주십시오.")
        @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 휴대전화 번호 형식이 아닙니다.")
        String phoneNumber,

        @Email
        @NotBlank(message = "이메일을 입력하여 주십시오.")
        @Pattern(regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?", message = "올바른 이메일 형식이 아닙니다.")
        String email
) {
}
