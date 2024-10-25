package com.nhnacademy.heukbaekbookshop.admin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 *
 * DB에서 추가하는 admin은 validation 값 필요
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "admin_login_id")
    private String loginId;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "admin_password")
    private String password;

}
