package com.nhnacademy.heukbaekbookshop.member.service.impl;

import com.nhnacademy.heukbaekbookshop.member.domain.*;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.exception.InvalidPasswordException;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.member.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

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
        if (memberRepository.existsByEmail(memberCreateRequest.email())) {
            throw new MemberAlreadyExistException();
        }

        Grade grade = gradeRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        // bCryptPasswordEncoder.encode(memberCreateRequest.password())

        Member member = memberCreateRequest.toMemberEntity(grade, memberCreateRequest.password());
        customerRepository.save(member);

        MemberAddress memberAddress = memberCreateRequest.toMemberAddressEntity(member);
        memberAddressRepository.save(memberAddress);

        return MemberResponse.from(member);
    }

    @Override
    public MemberResponse getMember(Long customerId) {
        return MemberResponse.from(memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new));
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long customerId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);

//        if (Objects.nonNull(memberUpdateRequest.oldPassword()) && !bCryptPasswordEncoder.matches(member.getPassword(), memberUpdateRequest.oldPassword())) {
//            throw new InvalidPasswordException();
//        }
        if (Objects.nonNull(memberUpdateRequest.newPassword()) &&
                memberUpdateRequest.oldPassword().equals(memberUpdateRequest.newPassword())) {
            throw new InvalidPasswordException();
        }

        return MemberResponse.from(member.modifyMember(memberUpdateRequest));
    }

    @Override
    @Transactional
    public MemberResponse changeMemberStatus(Long customerId, MemberStatus newStatus) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);
        member.setStatus(newStatus);
        return MemberResponse.from(member);
    }

}
