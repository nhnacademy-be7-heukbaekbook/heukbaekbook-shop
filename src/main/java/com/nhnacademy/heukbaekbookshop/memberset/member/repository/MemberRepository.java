package com.nhnacademy.heukbaekbookshop.memberset.member.repository;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}
