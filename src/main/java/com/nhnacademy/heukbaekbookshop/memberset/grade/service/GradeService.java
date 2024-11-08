package com.nhnacademy.heukbaekbookshop.memberset.grade.service;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;

import java.util.List;

public interface GradeService {
    GradeDto createGrade(GradeDto gradeDto);

    GradeDto getGrade(Long gradeId);

    List<GradeDto> getAllGradeList();

    GradeDto updateGrade(Long gradeId, GradeDto gradeDto);

    void deleteGrade(Long gradeId);

}
