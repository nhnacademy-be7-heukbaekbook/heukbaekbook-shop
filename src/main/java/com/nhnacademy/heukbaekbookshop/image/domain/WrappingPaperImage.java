package com.nhnacademy.heukbaekbookshop.image.domain;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wrapping_papers_images")
public class WrappingPaperImage {

    @Id
    @Column(name = "image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private Image image;

    @OneToOne
    @JoinColumn(name = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

}
