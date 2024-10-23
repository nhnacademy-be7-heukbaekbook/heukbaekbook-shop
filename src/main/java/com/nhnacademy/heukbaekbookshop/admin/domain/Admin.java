package com.nhnacademy.heukbaekbookshop.admin.domain;

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
@Table(name = "admins")
public class Admin {

    @Id
    @Column(name = "admin_id")
    private long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "admin_login_id")
    private String loginId;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "admin_password")
    private String password;

}
