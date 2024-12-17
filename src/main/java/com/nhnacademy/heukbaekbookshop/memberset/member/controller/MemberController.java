package com.nhnacademy.heukbaekbookshop.memberset.member.controller;


import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.MemberStatus;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.OAuthMemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MyPageResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final LikeService likeService;

    public static final String X_USER_ID = "X-USER-ID";

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

    @PostMapping("/oauth")
    public ResponseEntity<MemberResponse> createOAuthMember(@Valid @RequestBody OAuthMemberCreateRequest oAuthMemberCreateRequest, BindingResult result) {
        if (result.hasErrors()) {
            log.info(result.getAllErrors().toString());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.createOAuthMember(oAuthMemberCreateRequest));
    }

    /**
     * 회원 조회 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 조회를 위한 회원의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping
    public ResponseEntity<MemberResponse> getMember(@RequestHeader(X_USER_ID) Long customerId) {
        log.info("customerId: {}", customerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(customerId));
    }

    /**
     * 회원 수정 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 존재 확인을 위한 회원의 id 입니다.
     * @param memberUpdateRequest 회원 수정 dto 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(@RequestHeader(X_USER_ID) Long customerId, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(customerId, memberUpdateRequest));
    }

    /**
     * 회원 탈퇴 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 존재 확인을 위한 회원의 id 입니다.
     * @return 성공 시, 응답코드 204 반환합니다.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@RequestHeader(X_USER_ID) Long customerId){
        memberService.changeMemberStatus(customerId, MemberStatus.WITHDRAWN);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 회원 좋아요 조회 요청 시 사용되는 메서드입니다.
     *
     * @param customerId 회원 조회를 위한 회원의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/likes")
    public ResponseEntity<List<BookDetailResponse>> getLikedBooks(@RequestHeader(X_USER_ID) Long customerId) {
        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(customerId);
        return ResponseEntity.ok(likedBooks);
    }

    /**
     * 회원가입의 아이디 중복 확인 요청 시 사용되는 메서드입니다.
     *
     * @param loginId 중복 확인을 위한 회원의 입력 loginId 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/existsLoginId/{loginId}")
    public ResponseEntity<Boolean> existsLoginId(@PathVariable String loginId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.existsLoginId(loginId));
    }
    /**
     * 회원가입의 이메일 중복 확인 요청 시 사용되는 메서드입니다.
     *
     * @param email 중복 확인을 위한 회원의 입력 email 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/existsEmail/{email}")
    public ResponseEntity<Boolean> existsEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.existsEmail(email));
    }

    @GetMapping("/detail")
    public MemberDetailResponse getMemberDetail(@RequestHeader(X_USER_ID) Long customerId) {
        log.info("customerId: {}", customerId);

        return memberService.getMemberDetail(customerId);
    }

    @GetMapping("/grade")
    public GradeDto getMemberGrade(@RequestHeader(X_USER_ID) Long customerId) {
        return memberService.getMembersGrade(customerId);
    }

    @GetMapping("/my-page")
    public MyPageResponse getMyPageResponse(@RequestHeader(X_USER_ID) Long customerId,
                                            Pageable pageable) {
        log.info("customerId: {}, pageable : {}", customerId, pageable);

        return memberService.getMyPageResponse(customerId, pageable);
    }

    @GetMapping("/orders/{orderId}")
    public MyPageOrderDetailResponse getMyPageOrderDetailResponse(@RequestHeader(X_USER_ID) Long customerId,
                                                                  @PathVariable String orderId) {
        log.info("tossOrderId: {}", orderId);

        return memberService.getMyPageDetailResponse(customerId, orderId);
    }
}
