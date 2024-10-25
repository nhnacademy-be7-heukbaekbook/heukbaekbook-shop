package com.nhnacademy.heukbaekbookshop.member.dto.request;


import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberAddress;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

public record MemberCreateRequest(
        @NotBlank(message = "아이디를 입력하여 주십시오.")
        @Pattern(regexp = "^[a-z]{1}[a-z0-9]{2,20}", message = "올바른 아이디 형식이 아닙니다. 아이디는 4~16자로 영문 소문자, 숫자만 사용할 수 있습니다.")
        String loginId,

        @NotBlank(message = "비밀번호를 입력하여 주십시오.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "올바른 비밀번호 형식이 아닙니다. 비밀번호는 8~16자로 영문 대*소문자, 숫자, 특수문자가 각각 1개 이상이여야 합니다.")
        String password,

        @NotBlank(message = "생년월일을 입력하여 주십시오.")
        @Pattern(regexp = "^(19[0-9][0-9]|20[0-2][0-9])-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "올바른 생년월일 형식이 아닙니다.")
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
        String email,

        @NotBlank(message = "우편번호를 입력하여 주십시오.")
        Long postalCode,

        @NotBlank(message = "도로명 주소를 입력하여 주십시오.")
        String roadNameAddress,

        @NotBlank(message = "상세 주소를 입력하여 주십시오.")
        @Length(min = 1, max = 20)
        String detailAddress,

        @NotBlank(message = "주소의 별칭을 입력하여 주십시오.")
        @Pattern(regexp = "^[가-힣]{2,10}", message = "올바른 주소 별칭 형식이 아닙니다.")
        String alias
) {

    public Customer toCustomerEntity() {
        return Customer.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .build();
    }

    public Member toMemberEntity(Customer customer, Grade grade, String encodedPassword) {
        return Member.builder()
                .customer(customer)
                .loginId(this.loginId)
                .password(encodedPassword)
                .birth(this.birth)
                .grade(grade)
                .build();
    }

    public MemberAddress toMemberAddressEntity(Member member) {
        return MemberAddress.builder()
                .member(member)
                .postalCode(this.postalCode)
                .roadNameAddress(this.roadNameAddress)
                .detailAddress(this.detailAddress)
                .alias(this.alias)
                .build();
    }
}
