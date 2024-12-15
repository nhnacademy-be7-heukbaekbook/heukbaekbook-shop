package com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.mapper.GradeMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.OAuthMemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;

public class MemberMapper {

    private MemberMapper() {}

    public static Member createMemberEntity(MemberCreateRequest memberCreateRequest, Grade grade, String encodedPassword) {
        return Member.builder()
                .name(memberCreateRequest.name())
                .phoneNumber(memberCreateRequest.phoneNumber())
                .email(memberCreateRequest.email())
                .loginId(memberCreateRequest.loginId())
                .password(encodedPassword)
                .birth(memberCreateRequest.birth())
                .grade(grade)
                .build();
    }

    public static Member createOAuthMemberEntity(OAuthMemberCreateRequest oAuthMemberCreateRequest, Grade grade, String encodedPassword) {
        return Member.builder()
                .name(oAuthMemberCreateRequest.name())
                .phoneNumber(oAuthMemberCreateRequest.phoneNumber())
                .email(oAuthMemberCreateRequest.email())
                .loginId(oAuthMemberCreateRequest.loginId())
                .password(encodedPassword)
                .birth(oAuthMemberCreateRequest.birth())
                .grade(grade)
                .build();
    }

    public static MemberAddress createMemberAddressEntity(MemberCreateRequest memberCreateRequest,Member member) {
        return MemberAddress.builder()
                .member(member)
                .postalCode(memberCreateRequest.postalCode())
                .roadNameAddress(memberCreateRequest.roadNameAddress())
                .detailAddress(memberCreateRequest.detailAddress())
                .alias(memberCreateRequest.alias())
                .build();
    }

    public static MemberResponse createMemberResponse(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getLoginId(),
                member.getBirth(),
                member.getCreatedAt(),
                member.getLastLoginAt(),
                member.getStatus(),
                GradeMapper.createGradeResponse(member.getGrade())
        );
    }
}
