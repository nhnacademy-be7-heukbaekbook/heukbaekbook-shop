package com.nhnacademy.heukbaekbookshop.memberset.grade.domain;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grades")
public class Grade {

    @Id
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "grade_name")
    private String gradeName;

    @NotNull
    @Column(name = "grade_point_percentage")
    private BigDecimal pointPercentage;

    @NotNull
    @Column(name = "promotion_standard")
    private BigDecimal promotionStandard;

    @Builder
    public Grade(String gradeName, BigDecimal pointPercentage, BigDecimal promotionStandard) {
        this.gradeName = gradeName;
        this.pointPercentage = pointPercentage;
        this.promotionStandard = promotionStandard;
    }

    public Grade modifyGrade(GradeDto gradeDto) {
        this.gradeName = gradeDto.gradeName();
        this.promotionStandard = gradeDto.promotionStandard();
        this.pointPercentage = gradeDto.pointPercentage();
        return this;
    }
}
