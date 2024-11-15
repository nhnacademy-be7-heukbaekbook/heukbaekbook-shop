package com.nhnacademy.heukbaekbookshop.point.history.domain;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

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