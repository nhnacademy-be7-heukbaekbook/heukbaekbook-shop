package com.nhnacademy.heukbaekbookshop.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
