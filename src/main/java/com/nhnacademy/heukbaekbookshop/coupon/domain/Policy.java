package com.nhnacademy.heukbaekbookshop.coupon.domain;

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
@Table(name = "policies")
public class Policy {

    @Id
    @Column(name = "policy_id")
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "policy_discount_type")
    private DisCountType discountType;

    @NotNull
    @Column(name = "minumum_purchase_amount")
    private BigDecimal minimumPurchaseAmount;

    @Column(name = "maximum_discount_amount")
    private BigDecimal maximumDiscountAmount;

    @NotNull
    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "policy_name")
    private String name;

}
