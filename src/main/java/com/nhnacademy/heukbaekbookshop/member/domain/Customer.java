package com.nhnacademy.heukbaekbookshop.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private long id;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "customer_name")
    private String name;

    @NotNull
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 휴대전화 번호 형식이 아닙니다.")
    @Column(name = "customer_phone_number")
    private String phoneNumber;

    @NotNull
    @Email
    @Length(max = 30)
    @Column(name = "customer_email")
    private String email;

}