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

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wrapping_papers")
public class WrappingPaper {

    @Id
    @Column(name = "wrapping_paper_id")
    private Long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "wrapping_paper_name")
    private String name;

    @NotNull
    @Column(name = "wrapping_paper_price")
    private BigDecimal price;
}
