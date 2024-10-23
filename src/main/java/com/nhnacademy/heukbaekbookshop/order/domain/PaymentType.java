package com.nhnacademy.heukbaekbookshop.order.domain;

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
@Table(name = "payments_types")
public class PaymentType {

    @Id
    @Column(name = "payment_type_id")
    private long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "payment_type_name")
    private String name;
}
