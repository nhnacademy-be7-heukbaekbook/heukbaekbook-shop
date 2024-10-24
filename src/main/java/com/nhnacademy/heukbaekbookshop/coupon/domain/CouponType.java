package com.nhnacademy.heukbaekbookshop.coupon.domain;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons_types")
public class CouponType {

    @Id
    @Column(name = "coupon_type_id")
    private Long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "coupon_type_name")
    private String name;

}
