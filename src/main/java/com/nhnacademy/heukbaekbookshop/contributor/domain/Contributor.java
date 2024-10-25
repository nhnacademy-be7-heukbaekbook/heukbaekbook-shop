package com.nhnacademy.heukbaekbookshop.contributor.domain;

import jakarta.persistence.*;
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
@Table(name = "contributors")
public class Contributor {

    @Id
    @Column(name = "contributor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "contributor_name")
    private String name;

    @NotNull
    @Column(name = "contributor_description")
    private String description;

}