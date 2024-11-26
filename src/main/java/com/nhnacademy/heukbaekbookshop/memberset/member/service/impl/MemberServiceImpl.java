package com.nhnacademy.heukbaekbookshop.memberset.member.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper.MemberMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageResponse;
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
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderBookResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderSummaryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.event.SignupEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final CustomerRepository customerRepository;
    private final GradeRepository gradeRepository;
    private final ApplicationEventPublisher eventPublisher;

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

        eventPublisher.publishEvent(new SignupEvent(member.getId()));
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

    @Override
    public MemberDetailResponse getMemberDetail(Long customerId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new);

        return new MemberDetailResponse(
                member.getId(),
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getMemberAddresses().stream()
                        .map(memberAddress -> new MemberAddressResponse(
                                memberAddress.getId(),
                                memberAddress.getPostalCode(),
                                memberAddress.getRoadNameAddress(),
                                memberAddress.getDetailAddress(),
                                memberAddress.getAlias()
                        )).collect(Collectors.toList())
        );
    }

    @Override
    public MyPageResponse getMyPageResponse(Long customerId) {
        Member member = memberRepository.searchByCustomerId(customerId)
                .orElseThrow(MemberNotFoundException::new);

        MemberResponse memberResponse = new MemberResponse(
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getLoginId(),
                member.getBirth(),
                member.getCreatedAt(),
                member.getLastLoginAt(),
                member.getStatus(),
                new GradeDto(
                        member.getGrade().getGradeName(),
                        member.getGrade().getPointPercentage(),
                        member.getGrade().getPromotionStandard()
                )
        );

        List<OrderSummaryResponse> orderSummaryResponses = member.getOrders().stream()
                .map(order -> new OrderSummaryResponse(
                        order.getCreatedAt().toLocalDate(),
                        order.getTossOrderId(),
                        order.getStatus().name(),
                        null
                ))
                .toList();

        return new MyPageResponse(memberResponse, new OrderResponse(orderSummaryResponses));
    }

    @Override
    @Transactional
    public void changeMemberStatus(Long customerId, MemberStatus newStatus) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);
        member.setStatus(newStatus);
    }

}
