package com.nhnacademy.heukbaekbookshop.memberset.member.repository.impl;

import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        Grade grade = Grade.builder()
                .gradeName("골드")
                .promotionStandard(BigDecimal.valueOf(10000))
                .pointPercentage(BigDecimal.valueOf(5))
                .build();

        gradeRepository.save(grade);

        Member member1 = Member.builder()
                .name("정동현")
                .phoneNumber("010-1234-5678")
                .email("wjdehdgus@gmail.com")
                .loginId("test1")
                .password("1234")
                .birth(Date.valueOf(LocalDate.now()))
                .grade(grade)
                .build();

        Member member2 = Member.builder()
                .name("홍길동")
                .phoneNumber("010-1234-5677")
                .email("wjdehdgus1@gmail.com")
                .loginId("test2")
                .password("1234")
                .birth(Date.valueOf(LocalDate.now()))
                .grade(grade)
                .build();

        Member member3 = Member.builder()
                .name("이순신")
                .phoneNumber("010-1234-5678")
                .email("wjdehdgus2@gmail.com")
                .loginId("test3")
                .password("1234")
                .birth(Date.valueOf(LocalDate.now()))
                .grade(grade)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    void searchByCustomerId() {
        //given
        Long customerId = memberRepository.findAll().getFirst().getId();

        //when
        Optional<Member> result = memberRepository.searchByCustomerId(customerId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(customerId);
    }

    @Test
    void findGradeByMemberId() {
        //given
        Long customerId = memberRepository.findAll().getFirst().getId();

        //when
        Optional<Grade> result = memberRepository.findGradeByMemberId(customerId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
}