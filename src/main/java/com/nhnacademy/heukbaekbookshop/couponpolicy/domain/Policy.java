package com.nhnacademy.heukbaekbookshop.couponpolicy.domain;

import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.DisCountType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Policy(DisCountType disCountType, BigDecimal minimumPurchaseAmount, BigDecimal maximumDiscountAmount, BigDecimal discountValue) {
        this.discountType = disCountType;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.discountValue = discountValue;
    }

}