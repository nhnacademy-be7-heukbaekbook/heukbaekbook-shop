package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "wrapping_paper_name")
    private String name;

    @NotNull
    @Column(name = "wrapping_paper_price")
    private BigDecimal price;

    @OneToOne(mappedBy = "wrappingPaper", fetch = FetchType.LAZY)
    private WrappingPaperImage wrappingPaperImage;

    public static WrappingPaper createWrappingPaper(String name, BigDecimal price) {
        WrappingPaper wrappingPaper = new WrappingPaper();
        wrappingPaper.setName(name);
        wrappingPaper.setPrice(price);
        return wrappingPaper;
    }
}
