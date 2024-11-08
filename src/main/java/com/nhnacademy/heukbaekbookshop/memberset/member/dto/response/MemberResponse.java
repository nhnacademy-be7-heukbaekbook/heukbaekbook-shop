package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.MemberStatus;

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
        GradeDto grade
) {
}
