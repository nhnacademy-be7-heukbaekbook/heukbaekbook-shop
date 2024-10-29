package com.nhnacademy.heukbaekbookshop.member.controller;


import com.nhnacademy.heukbaekbookshop.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Member(회원) RestController
 *
 * @author : 이승형
 * @date : 2024-10-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final LikeService likeService;

    /**
     * 회원 생성 요청 시 사용되는 메서드입니다.
     *
     * @param memberCreateRequest 회원 생성 dto 입니다.
     * @return 성공 시, 응답코드 201 반환합니다.
     */
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.createMember(memberCreateRequest));
    }

    /**
     * 회원 조회 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 조회를 위한 회원의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long customerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(customerId));
    }

    /**
     * 회원 수정 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 존재 확인을 위한 회원의 id 입니다.
     * @param memberUpdateRequest 회원 수정 dto 입니다.
     * @return 성공 시, 응답코드 204 반환합니다.
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long customerId, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(memberService.updateMember(customerId, memberUpdateRequest));
    }

    /**
     * 회원 탈퇴 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 존재 확인을 위한 회원의 id 입니다.
     * @return 성공 시, 응답코드 204 반환합니다.
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long customerId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(memberService.changeMemberStatus(customerId, MemberStatus.WITHDRAWN));
    }

    @PostMapping("/{customerId}/likes")
    public ResponseEntity<List<BookDetailResponse>> getLikedBooks(@PathVariable Long customerId) {
        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(customerId);
        return ResponseEntity.ok(likedBooks);
    }
}
