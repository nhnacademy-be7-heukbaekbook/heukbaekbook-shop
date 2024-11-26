package com.nhnacademy.heukbaekbookshop.memberset.member.repository.impl;


import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.memberset.grade.domain.QGrade.*;
import static com.nhnacademy.heukbaekbookshop.memberset.member.domain.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> searchByCustomerId(Long customerId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .join(member.grade, grade)
                .fetchOne());
    }
}
