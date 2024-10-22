package com.nhnacademy.heukbaekbookshop.point.domain;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
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
@Table(name = "points_histories")
public class PointHistory {

    @Id
    @Column(name = "point_history_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    // TODO #2: 주문번호 외래 키 추가

    @ManyToOne
    @JoinColumn(name = "point_earn_standard_id")
    private PointEarnStandard pointEarnStandard;

    @NotNull
    @Column(name = "point_amount")
    private BigDecimal amount;

    @NotNull
    @Column(name = "point_created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "point_balance")
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_type")
    private PointType type;

}
