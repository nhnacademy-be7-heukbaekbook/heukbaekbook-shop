package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.member.domain.Customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFee deliveryFee;

    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "order_cretaed_at")
    private LocalDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "order_customer_name")
    private String customerName;

    @NotNull
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 휴대전화 번호 형식이 아닙니다.")
    @Column(name = "order_customer_phone_number")
    private String customerPhoneNumber;

    @NotNull
    @Email
    @Length(max = 30)
    @Column(name = "order_customer_email")
    private String customerEmail;

}
