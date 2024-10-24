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
    private long id;

    @NotNull
    @Column(name = "contributor_name")
    @Length(min = 1, max = 10)
    private String name;

    @NotNull
    @Column(name = "contributor_description")
    private String description;

}