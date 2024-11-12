package com.nhnacademy.heukbaekbookshop.memberset.grade.controller;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.grade.service.GradeService;
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
 * @date : 2024-10-31
 */
@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    /**
     * 등급 단일 조회 요청 시 사용되는 메서드입니다.
     *
     * @param gradeId 회원 등급 조회를 위한 등급의 id 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/grades/{gradeId}")
    public ResponseEntity<GradeDto> getGrade(@PathVariable Long gradeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gradeService.getGrade(gradeId));
    }

    /**
     * 등급 전체 조회 요청 시 사용되는 메서드입니다.
     *
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @GetMapping("/grades")
    public ResponseEntity<List<GradeDto>> getAllGrades() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gradeService.getAllGradeList());
    }

    /**
     * 등급 생성 요청 시 사용되는 메서드입니다.
     *
     * @param gradeDto 등급 생성 dto 입니다.
     * @return 성공 시, 응답코드 201 반환합니다.
     */
    @PostMapping("/admin/grades")
    public ResponseEntity<GradeDto> createGrade(@Valid @RequestBody GradeDto gradeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gradeService.createGrade(gradeDto));
    }

    /**
     * 등급 수정 요청 시 사용되는 메서드입니다.
     *
     * @param gradeId 등급 조회를 위한 등급의 id 입니다.
     * @param gradeDto 회원 등급 수정 dto 입니다.
     * @return 성공 시, 응답코드 200 반환합니다.
     */
    @PutMapping("/admin/grades/{gradeId}")
    public ResponseEntity<GradeDto> updateGrade(@PathVariable Long gradeId, @Valid @RequestBody GradeDto gradeDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gradeService.updateGrade(gradeId,gradeDto));
    }

    /**
     * 등급 삭제 요청 시 사용되는 메서드입니다.
     *
     * @param gradeId 등급 존재 확인을 위한 등급의 id 입니다.
     * @return 성공 시, 응답코드 204 반환합니다.
     */
    @DeleteMapping("/admin/grades/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
