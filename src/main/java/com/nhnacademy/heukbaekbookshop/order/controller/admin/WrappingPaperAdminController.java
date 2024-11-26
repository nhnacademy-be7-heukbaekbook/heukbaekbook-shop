package com.nhnacademy.heukbaekbookshop.order.controller.admin;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.service.WrappingPaperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/wrapping-papers")
@RequiredArgsConstructor
public class WrappingPaperAdminController {
    private final WrappingPaperAdminService wrappingPaperAdminService;

    // 포장지 등록
    @PostMapping
    public ResponseEntity<WrappingPaper> addWrappingPaper(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestPart(required = false) MultipartFile imageFile) {
        WrappingPaper wrappingPaper = wrappingPaperAdminService.addWrappingPaper(name, price, imageFile);
        return new ResponseEntity<>(wrappingPaper, HttpStatus.CREATED);
    }

    // 포장지 수정
    @PutMapping("/{id}")
    public ResponseEntity<WrappingPaper> updateWrappingPaper(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestPart(required = false) MultipartFile imageFile) {
        WrappingPaper wrappingPaper = wrappingPaperAdminService.updateWrappingPaper(id, name, price, imageFile);
        return ResponseEntity.ok(wrappingPaper);
    }

    // 포장지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWrappingPaper(@PathVariable Long id) {
        wrappingPaperAdminService.deleteWrappingPaper(id);
        return ResponseEntity.noContent().build();
    }



}
