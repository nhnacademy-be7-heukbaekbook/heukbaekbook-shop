package com.nhnacademy.heukbaekbookshop.member.service;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;

import java.util.Optional;

public interface MemberService {

    MemberResponse createMember(MemberCreateRequest memberCreateRequest);

    Optional<Member> getMember(Long memberId);
}
