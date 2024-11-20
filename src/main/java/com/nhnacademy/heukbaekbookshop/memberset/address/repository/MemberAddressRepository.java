package com.nhnacademy.heukbaekbookshop.memberset.address.repository;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    List<MemberAddress> getAllByMemberIdOrderByCreatedAtDesc(Long customerId);

    Long countByMemberId(Long customerId);

    boolean existsByMemberAndPostalCodeAndDetailAddress(Member member, Long postalCode, String detailAddress);
}
