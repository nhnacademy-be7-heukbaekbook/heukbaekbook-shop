package com.nhnacademy.heukbaekbookshop.point.earn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "point_earn_type")
    private PointEarnType pointEarnType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_earn_standard_status")
    private PointEarnStandardStatus status;

    @NotNull
    @Column(name = "point_earn_start")
    private LocalDateTime pointEarnStart;

    @Column(name = "point_earn_end")
    private LocalDateTime pointEarnEnd;

    @ManyToOne
    @JoinColumn(name = "point_earn_event_id", nullable = false)
    private PointEarnEvent pointEarnEvent;

}
