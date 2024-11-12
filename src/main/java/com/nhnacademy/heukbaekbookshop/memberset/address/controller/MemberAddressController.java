package com.nhnacademy.heukbaekbookshop.memberset.address.controller;


import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.MemberAddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Member(회원) RestController
 *
 * @author : 이승형
 * @date : 2024-10-29
 */
@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping("/api/members/addresses")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    public static final String X_USER_ID = "X-USER-ID";

    /**
     * 회원 주소 생성 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 주소 생성을 위한 회원의 id 입니다.
     * @param memberAddressDto 회원 주소 생성 dto 입니다.
     * @return 성공 시, 응답코드 201 반환합니다.
     */
    @Transactional
    @PostMapping
    public ResponseEntity<MemberAddressDto> createMemberAddress(@RequestHeader(X_USER_ID) Long customerId, @Valid @RequestBody MemberAddressDto memberAddressDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberAddressService.createMemberAddress(customerId, memberAddressDto));
    }

    /**
     * 회원 주소 단일 조회 요청 시 사용되는 메서드입니다.
     *
     * @param addressId 회원 주소 조회를 위한 주소의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> getMemberAddress(@PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.getMemberAddress(addressId));
    }

    /**
     * 회원이 등록한 주소 전체 조회 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 주소 전체 조회를 위한 주소의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<MemberAddressDto>> getAllMemberAddresses(@RequestHeader(X_USER_ID) Long customerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.getMemberAddressesList(customerId));
    }

    /**
     * 회원 주소 수정 요청 시 사용되는 메서드입니다.
     *
     * @param addressId 회원 주소 조회를 위한 주소의 id 입니다.
     * @param memberAddressDto 회원 주소 수정 dto 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @Transactional
    @PutMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> updateMemberAddress(@PathVariable Long addressId, @Valid @RequestBody MemberAddressDto memberAddressDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.updateMemberAddress(addressId, memberAddressDto));
    }

    /**
     * 회원 주소 삭제 요청 시 사용되는 메서드입니다.
     *
     * @param addressId 회원 주소 존재 확인을 위한 주소의 id 입니다.
     * @return 성공 시, 응답코드 204 반환합니다.
     */
    @Transactional
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteMemberAddress(@PathVariable Long addressId) {
        memberAddressService.deleteMemberAddress(addressId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMemberAddresses(@RequestHeader(X_USER_ID) Long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberAddressService.countByMemberId(customerId));
    }
}
