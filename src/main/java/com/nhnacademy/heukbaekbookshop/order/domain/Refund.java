package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refunds")
public class Refund {

    @Id
    @Column(name = "refund_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reason")
    private String reason;

    @NotNull
    @Column(name = "refund_requested_at")
    private LocalDateTime refundRequestAt;

    @NotNull
    @Column(name = "refund_approved_at")
    private LocalDateTime refundApprovedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @OneToMany(mappedBy = "refund")
    private Set<OrderBookRefund> orderBookReturns = new HashSet<>();
}