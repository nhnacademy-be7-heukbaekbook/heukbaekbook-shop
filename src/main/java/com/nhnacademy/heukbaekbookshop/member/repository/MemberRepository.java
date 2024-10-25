package com.nhnacademy.heukbaekbookshop.member.repository;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByMemberId(String memberId);

}
