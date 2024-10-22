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
@IdClass(WrappingPaperImageId.class)
@Table(name = "wrapping_papers_images")
public class WrappingPaperImage {

    @Id
    @Column(name = "wrapping_paper_id")
    private long wrappingPaperId;

    @Id
    @Column(name = "image_id")
    private long imageId;

    @OneToOne
    @MapsId("wrappingPaperId")
    @JoinColumn(name = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

    @OneToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private Image image;
}
