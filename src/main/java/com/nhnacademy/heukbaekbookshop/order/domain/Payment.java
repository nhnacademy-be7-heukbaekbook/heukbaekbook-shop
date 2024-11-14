package com.nhnacademy.heukbaekbookshop.order.domain;

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
@Table(name = "payments")
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    @Enumerated(EnumType.STRING)
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

}
