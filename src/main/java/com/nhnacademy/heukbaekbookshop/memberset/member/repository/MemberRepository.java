package com.nhnacademy.heukbaekbookshop.memberset.member.repository;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}
