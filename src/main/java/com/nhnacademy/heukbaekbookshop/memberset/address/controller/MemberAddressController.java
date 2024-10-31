package com.nhnacademy.heukbaekbookshop.memberset.address.controller;


import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.MemberAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping("/api/members/{customerId}/addresses")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    @Transactional
    @PostMapping
    public ResponseEntity<MemberAddressDto> createMemberAddress(@PathVariable Long customerId, @Valid @RequestBody MemberAddressDto memberAddressDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberAddressService.createMemberAddress(customerId, memberAddressDto));
    }

    @GetMapping
    public ResponseEntity<List<MemberAddressDto>> getAllMemberAddresses(@PathVariable Long customerId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.getMemberAddressesList(customerId));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> getMemberAddress(@PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.getMemberAddress(addressId));
    }

    @Transactional
    @PutMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> updateMemberAddress(@PathVariable Long addressId, @Valid @RequestBody MemberAddressDto memberAddressDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberAddressService.updateMemberAddress(addressId, memberAddressDto));
    }

    @Transactional
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteMemberAddress(@PathVariable Long addressId) {
        memberAddressService.deleteMemberAddress(addressId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
