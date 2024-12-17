package com.nhnacademy.heukbaekbookshop.memberset.customer.domain;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "customer_name")
    protected String name;

    @NotNull
    @Column(name = "customer_phone_number")
    protected String phoneNumber;

    @Email
    @NotNull
    @Column(name = "customer_email")
    protected String email;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    public static Customer createCustomer(String name, String phoneNumber, String email) {
        Customer customer = new Customer();
        customer.name = name;
        customer.phoneNumber = phoneNumber;
        customer.email = email;
        return customer;
    }

    protected Customer(String name, String phoneNumber, String email){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
