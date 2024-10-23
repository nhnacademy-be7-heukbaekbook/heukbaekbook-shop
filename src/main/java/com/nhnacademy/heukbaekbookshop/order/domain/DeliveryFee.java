package com.nhnacademy.heukbaekbookshop.order.domain;

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
@Table(name = "delivery_fees")
public class DeliveryFee {

    @Id
    @Column(name = "delivery_fee_id")
    private long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "delivery_fee_name")
    private String name;

    @NotNull
    @Column(name = "delivery_fee")
    private BigDecimal fee;
}
