package com.nhnacademy.heukbaekbookshop.member.service;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;

import java.util.Optional;

public interface MemberService {

    MemberResponse createMember(MemberCreateRequest memberCreateRequest);

    MemberResponse getMember(Long customerId);

    MemberResponse changeMemberStatus(Long customerId, MemberStatus memberStatus);

    MemberResponse updateMember(Long customerId, MemberUpdateRequest memberUpdateRequest);
}
