package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "order_cretaed_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    @Setter
    private OrderStatus status;

    @Column(name = "order_customer_name")
    private String customerName;

    @Column(name = "order_customer_phone_number")
    private String customerPhoneNumber;

    @Column(name = "order_customer_email")
    private String customerEmail;

    @Column(name = "toss_order_id")
    private String tossOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFee deliveryFee;

    @OneToMany(mappedBy = "order")
    private List<OrderBook> orderBooks = new ArrayList<>();

    private void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getOrders().add(this);
    }

    public static Order createOrder(BigDecimal totalPrice,
                                    String customerName,
                                    String customerPhoneNumber,
                                    String customerEmail,
                                    String tossOrderId,
                                    Customer customer,
                                    DeliveryFee deliveryFee) {
        Order order = new Order();
        order.totalPrice = totalPrice;
        order.customerName = customerName;
        order.customerPhoneNumber = customerPhoneNumber;
        order.customerEmail = customerEmail;
        order.tossOrderId = tossOrderId;
        order.setCustomer(customer);
        order.deliveryFee = deliveryFee;

        return order;
    }

}
