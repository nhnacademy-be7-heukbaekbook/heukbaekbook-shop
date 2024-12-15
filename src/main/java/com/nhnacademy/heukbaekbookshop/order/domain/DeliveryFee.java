package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_fees")
public class DeliveryFee {

    @Id
    @Column(name = "delivery_fee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "delivery_fee_name")
    private String name;

    @NotNull
    @Column(name = "delivery_fee")
    private BigDecimal fee;

    @NotNull
    @Column(name = "minimum_order_amount")
    private BigDecimal minimumOrderAmount;

    @Builder
    public DeliveryFee(String name, BigDecimal fee, BigDecimal minimumOrderAmount) {
        this.name = name;
        this.fee = fee;
        this.minimumOrderAmount = minimumOrderAmount;
    }

}
