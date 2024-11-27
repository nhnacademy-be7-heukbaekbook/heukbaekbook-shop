package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Builder
    public Refund(Long id, String reason, LocalDateTime refundRequestAt, LocalDateTime refundApprovedAt, RefundStatus refundStatus) {
        this.id = id;
        this.reason = reason;
        this.refundRequestAt = refundRequestAt;
        this.refundApprovedAt = refundApprovedAt;
        this.refundStatus = refundStatus;
    }
}
