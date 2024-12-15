package com.nhnacademy.heukbaekbookshop.image.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@Inheritance(strategy = InheritanceType.JOINED)
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_url")
    private String url;

    @Column(name = "image_type")
    @Enumerated(EnumType.STRING)
    private ImageType type;
}
