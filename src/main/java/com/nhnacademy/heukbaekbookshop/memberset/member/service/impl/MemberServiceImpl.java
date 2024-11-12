package com.nhnacademy.heukbaekbookshop.memberset.member.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper.MemberMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.InvalidPasswordException;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.memberset.address.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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

        Member member = MemberMapper.createMemberEntity(memberCreateRequest, grade, bCryptPasswordEncoder.encode(memberCreateRequest.password()));
        customerRepository.save(member);

        MemberAddress memberAddress = MemberMapper.createMemberAddressEntity(memberCreateRequest, member);
        memberAddressRepository.save(memberAddress);

        return MemberMapper.createMemberResponse(member);
    }

    @Override
    public MemberResponse getMember(Long customerId) {
        return MemberMapper.createMemberResponse(memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new));
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long customerId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);

        if (Objects.nonNull(memberUpdateRequest.oldPassword())
                && !bCryptPasswordEncoder.matches(memberUpdateRequest.oldPassword(), member.getPassword())) {
            throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다. 다시 시도해주세요");
        }
        if (Objects.nonNull(memberUpdateRequest.newPassword()) &&
                memberUpdateRequest.oldPassword().equals(memberUpdateRequest.newPassword())) {
            throw new InvalidPasswordException("현재 비밀번호와 새 비밀번호가 같습니다. 다시 시도해주세요");
        }

        return MemberMapper.createMemberResponse(member.modifyMember(memberUpdateRequest,
                Objects.nonNull(memberUpdateRequest.newPassword())
                        ? bCryptPasswordEncoder.encode(memberUpdateRequest.newPassword()) : null));
    }

    @Override
    public boolean existsLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

//    @Override
//    public boolean checkValidPassword(String oldPassword, String newPassword) {
//        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);
//
//        if (Objects.nonNull(oldPassword())
//                && !bCryptPasswordEncoder.matches(oldPassword(), member.getPassword())) {
//            throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다. 다시 시도해주세요");
//        }
//        if (Objects.nonNull(memberUpdateRequest.newPassword()) &&
//                memberUpdateRequest.oldPassword().equals(memberUpdateRequest.newPassword())) {
//            throw new InvalidPasswordException("현재 비밀번호와 새 비밀번호가 같습니다. 다시 시도해주세요");
//        }
//
//        return false;
//    }

    @Override
    @Transactional
    public void changeMemberStatus(Long customerId, MemberStatus newStatus) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);
        member.setStatus(newStatus);
    }

}
