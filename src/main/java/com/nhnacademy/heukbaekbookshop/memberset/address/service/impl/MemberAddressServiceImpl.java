package com.nhnacademy.heukbaekbookshop.memberset.address.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.mapper.MemberAddressMapper;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.AddressLimitExceededException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.address.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.MemberAddressService;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAddressServiceImpl implements MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberAddressResponse getMemberAddress(Long addressId) {
        return MemberAddressMapper.createMemberAddressResponse(
                memberAddressRepository.findById(addressId)
                        .orElseThrow(MemberAddressNotFoundException::new));
    }

    @Override
    public List<MemberAddressResponse> getMemberAddressesList(Long customerId) {
        return MemberAddressMapper.createMemberAddressResponseList(
                memberAddressRepository.getAllByMemberIdOrderByCreatedAtDesc(customerId));
    }

    @Override
    @Transactional
    public MemberAddressResponse createMemberAddress(Long customerId, MemberAddressRequest memberAddressRequest) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);

        if (countByMemberId(customerId) >= 10L) {
            throw new AddressLimitExceededException();
        }
        if (memberAddressRepository.existsByMemberAndPostalCodeAndDetailAddress(member, memberAddressRequest.postalCode(), memberAddressRequest.detailAddress().trim())) {
            throw new MemberAddressAlreadyExistsException();
        }

        MemberAddress memberAddress = memberAddressRepository.save(
                MemberAddressMapper.createMemberAddressEntity(memberAddressRequest, member));

        return MemberAddressMapper.createMemberAddressResponse(memberAddress);
    }

    @Override
    @Transactional
    public MemberAddressResponse updateMemberAddress(Long addressId, MemberAddressRequest memberAddressRequest) {
        MemberAddress memberAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(MemberAddressNotFoundException::new);

        return MemberAddressMapper.createMemberAddressResponse(
                memberAddress.modifyMemberAddress(memberAddressRequest));
    }

    @Override
    @Transactional
    public void deleteMemberAddress(Long addressId) {
        if (!memberAddressRepository.existsById(addressId)) {
            throw new MemberAddressNotFoundException();
        }
        memberAddressRepository.deleteById(addressId);
    }

    @Override
    public Long countByMemberId(Long customerId) {
        return memberAddressRepository.countByMemberId(customerId);
    }

}
