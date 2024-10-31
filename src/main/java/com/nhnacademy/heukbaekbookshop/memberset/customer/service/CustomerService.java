package com.nhnacademy.heukbaekbookshop.memberset.customer.service;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.customer.dto.request.CustomerCreateRequest;

import java.util.Optional;

public interface CustomerService {

    void createCustomer(CustomerCreateRequest customerCreateRequest);

    boolean existCustomerByMemberId(String memberId);

    Optional<Member> getMember(Long memberId);
}
