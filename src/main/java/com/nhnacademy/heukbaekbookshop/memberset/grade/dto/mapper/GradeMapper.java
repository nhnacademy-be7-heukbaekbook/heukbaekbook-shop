package com.nhnacademy.heukbaekbookshop.memberset.grade.dto.mapper;

import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;

import java.util.List;
import java.util.stream.Collectors;

public class GradeMapper {
    public static GradeDto createGradeResponse(Grade grade) {
        return new GradeDto(
                grade.getGradeName(),
                grade.getPointPercentage(),
                grade.getPromotionStandard()
        );
    }

    public static List<GradeDto> createGradeResponseList(List<Grade> gradeList) {
        return gradeList.stream().map(grade -> createGradeResponse(grade)).collect(Collectors.toList());
    }

    public static Grade createGradeEntity(GradeDto gradeDto) {
        return Grade.builder()
                .gradeName(gradeDto.gradeName())
                .pointPercentage(gradeDto.pointPercentage())
                .promotionStandard(gradeDto.promotionStandard())
                .build();
    }
}
