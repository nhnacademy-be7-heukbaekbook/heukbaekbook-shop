package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {

    @Id
    @Column(name = "order_id")
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "delivery_recipient")
    private String recipient;

    @NotNull
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 휴대전화 번호 형식이 아닙니다.")
    @Column(name = "delivery_recipient_phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "delivery_postal_code")
    private long postalCode;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "delivery_road_name_address")
    private String roadNameAddress;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "delivery_detail_address")
    private String detailAddress;

    @Column(name = "delivery_forwarding_date")
    private LocalDateTime forwardingDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

}
