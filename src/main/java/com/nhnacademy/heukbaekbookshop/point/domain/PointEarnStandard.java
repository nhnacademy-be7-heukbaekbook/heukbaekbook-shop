package com.nhnacademy.heukbaekbookshop.point.domain;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "point_name")
    private String name;

    @NotNull
    @Column(name = "point")
    private BigDecimal point;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_earn_standard_status")
    private PointEarnStandardStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_earn_trigger_event")
    private PointEarnTriggerEvent triggerEvent;

}
