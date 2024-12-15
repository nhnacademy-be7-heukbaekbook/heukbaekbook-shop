package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @Column(name = "payment_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    @NotNull
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @NotNull
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @NotNull
    @Column(name = "payment_price")
    protected BigDecimal price;

    @Builder
    public Payment(String id, Order order, PaymentType paymentType, LocalDateTime requestedAt, LocalDateTime approvedAt, BigDecimal price) {
        this.id = id;
        this.order = order;
        this.paymentType = paymentType;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.price = price;
    }
}
