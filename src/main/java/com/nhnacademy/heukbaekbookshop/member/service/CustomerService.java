package com.nhnacademy.heukbaekbookshop.member.service;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.dto.request.CustomerCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;

import java.util.Optional;

public interface CustomerService {

    void createCustomer(CustomerCreateRequest customerCreateRequest);

    boolean existCustomerByMemberId(String memberId);

    Optional<Member> getMember(Long memberId);
}
