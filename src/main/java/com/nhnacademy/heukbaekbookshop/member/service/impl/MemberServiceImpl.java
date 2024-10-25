package com.nhnacademy.heukbaekbookshop.member.service.impl;

import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.member.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.member.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final CustomerRepository customerRepository;
    private final GradeRepository gradeRepository;

    @Override
    @Transactional
    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {
        if (memberRepository.existsByLoginId(memberCreateRequest.loginId())) {
            throw new MemberAlreadyExistException();
        }

        if (customerRepository.existsCustomerByEmail(memberCreateRequest.email())) {
            throw new MemberAlreadyExistException();
        }


        Grade grade = gradeRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        // bCryptPasswordEncoder.encode(memberCreateRequest.password())

        Member member = memberCreateRequest.toMemberEntity(grade, memberCreateRequest.password());
        System.out.println(member.toString());
        customerRepository.save(member);

        MemberAddress memberAddress = memberCreateRequest.toMemberAddressEntity(member);
        memberAddressRepository.save(memberAddress);

        return MemberResponse.from(member);
    }


    @Override
    public Optional<Member> getMember(Long memberId) {
        return Optional.empty();
    }
}
