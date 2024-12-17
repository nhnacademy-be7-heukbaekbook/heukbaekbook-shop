package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Column(name = "order_created_at")
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

    @OneToOne(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Setter
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Setter
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<PointHistory> pointHistories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFee deliveryFee;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OrderBook> orderBooks = new HashSet<>();

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
        order.createdAt = LocalDateTime.now();
        order.status = OrderStatus.WAITING_PAYMENT;
        order.customerName = customerName;
        order.customerPhoneNumber = customerPhoneNumber;
        order.customerEmail = customerEmail;
        order.tossOrderId = tossOrderId;
        order.setCustomer(customer);
        order.deliveryFee = deliveryFee;

        return order;
    }

}
