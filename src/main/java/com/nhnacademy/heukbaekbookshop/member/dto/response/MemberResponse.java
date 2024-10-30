package com.nhnacademy.heukbaekbookshop.member.dto.response;

import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberStatus;

import java.sql.Date;
import java.time.LocalDateTime;

public record MemberResponse(
        String name,
        String phoneNumber,
        String email,
        String loginId,
        Date birth,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt,
        MemberStatus memberStatus,
        Grade grade
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getLoginId(),
                member.getBirth(),
                member.getCreatedAt(),
                member.getLastLoginAt(),
                member.getStatus(),
                member.getGrade()
        );
    }
}
