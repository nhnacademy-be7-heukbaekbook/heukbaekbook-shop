package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries")
public class Delivery {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id", unique = true) // 조인 컬럼 매핑
    private Order order;

    @Column(name = "delivery_recipient")
    private String recipient;

    @Column(name = "delivery_recipient_phone_number")
    private String phoneNumber;

    @Column(name = "delivery_postal_code")
    private Long postalCode;

    @Column(name = "delivery_road_name_address")
    private String roadNameAddress;

    @Column(name = "delivery_detail_address")
    private String detailAddress;

    @Column(name = "delivery_forwarding_date")
    private LocalDateTime forwardingDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    private void setOrder(Order order) {
        this.order = order;
        order.setDelivery(this);
    }

    public static Delivery createDelivery(Order order, String recipient, String phoneNumber, Long postalCode, String roadNameAddress, String detailAddress, LocalDateTime forwardingDate, LocalDateTime deliveryDate) {
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.recipient = recipient;
        delivery.phoneNumber = phoneNumber;
        delivery.postalCode = postalCode;
        delivery.roadNameAddress = roadNameAddress;
        delivery.detailAddress = detailAddress;
        delivery.forwardingDate = forwardingDate;
        delivery.deliveryDate = deliveryDate;
        return delivery;
    }

}
