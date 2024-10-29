package com.nhnacademy.heukbaekbookshop.member.controller;


import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import com.nhnacademy.heukbaekbookshop.member.dto.request.MemberCreateRequest;
import com.nhnacademy.heukbaekbookshop.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(memberCreateRequest));
    }

    @PostMapping("/{customerId}/likes")
    public ResponseEntity<List<BookDetailResponse>> getLikedBooks(@PathVariable Long customerId) {
        List<BookDetailResponse> likedBooks = likeService.getLikedBooks(customerId);
        return ResponseEntity.ok(likedBooks);
    }
}
