package com.nhnacademy.heukbaekbookshop.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private GradeName grade;

    @NotNull
    @Column(name = "grade_point_percentage")
    private int pointPercentage;

    @NotNull
    @Column(name = "promotion_standard")
    private BigDecimal promotionStandard;
}
