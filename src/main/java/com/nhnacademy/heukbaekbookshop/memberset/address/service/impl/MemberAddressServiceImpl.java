package com.nhnacademy.heukbaekbookshop.memberset.address.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.mapper.MemberAddressMapper;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.AddressLimitExceededException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.address.repository.MemberAddressRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.MemberAddressService;
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
    public MemberAddressDto getMemberAddress(Long addressId) {
        return MemberAddressMapper.createMemberAddressResponse(
                memberAddressRepository.findById(addressId)
                        .orElseThrow(MemberAddressNotFoundException::new));
    }

    @Override
    public List<MemberAddressDto> getMemberAddressesList(Long customerId) {
        return MemberAddressMapper.createMemberAddressResponseList(
                memberAddressRepository.getAllByMemberIdOrderByCreatedAtDesc(customerId));
    }

    @Override
    public MemberAddressDto createMemberAddress(Long customerId, MemberAddressDto memberAddressDto) {
        Member member = memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new);

        if (memberAddressRepository.countByMemberId(customerId) >= 10L) {
            throw new AddressLimitExceededException();
        }
        if (memberAddressRepository.existsByPostalCodeAndDetailAddress(memberAddressDto.postalCode(), memberAddressDto.detailAddress())) {
            throw new MemberAddressAlreadyExistsException();
        }

        MemberAddress memberAddress = memberAddressRepository.save(
                MemberAddressMapper.createMemberAddressEntity(memberAddressDto, member));

        return MemberAddressMapper.createMemberAddressResponse(memberAddress);
    }

    @Override
    public MemberAddressDto updateMemberAddress(Long addressId, MemberAddressDto memberAddressDto) {
        MemberAddress memberAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(MemberAddressNotFoundException::new);

        return MemberAddressMapper.createMemberAddressResponse(
                memberAddress.modifyMemberAddress(memberAddressDto));
    }

    @Override
    public void deleteMemberAddress(Long addressId) {
        if (!memberAddressRepository.existsById(addressId)) {
            throw new MemberAddressNotFoundException();
        }
        memberAddressRepository.deleteById(addressId);
    }

}
