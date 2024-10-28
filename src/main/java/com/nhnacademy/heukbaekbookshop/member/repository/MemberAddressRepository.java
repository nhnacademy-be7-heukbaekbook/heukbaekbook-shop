package com.nhnacademy.heukbaekbookshop.member.repository;

import com.nhnacademy.heukbaekbookshop.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
