package com.nhnacademy.heukbaekbookshop.point.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "points_earns_standards")
public class PointEarnStandard {
    @Id
    @Column(name = "point_earn_standard_id")
    private long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "point_name")
    private String name;

    @NotNull
    @Column(name = "point")
    private BigDecimal point;

}
