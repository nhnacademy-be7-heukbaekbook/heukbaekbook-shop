package com.nhnacademy.heukbaekbookshop.tag.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "tag_id")
    private Long id;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "tag_name")
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> bookTags;

}