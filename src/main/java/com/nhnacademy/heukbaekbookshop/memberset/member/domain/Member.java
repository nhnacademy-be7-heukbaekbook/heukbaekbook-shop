package com.nhnacademy.heukbaekbookshop.memberset.member.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Like;
import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.request.MemberUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "members")
public class Member extends Customer {

    @NotNull
    @Column(name = "member_login_id")
    private String loginId;

    @NotNull
    @Column(name = "member_password")
    private String password;

    @NotNull
    @Column(name = "member_birth")
    private Date birth;

    @NotNull
    @Column(name = "member_created_at")
    private LocalDateTime createdAt;

    @Column(name = "member_last_login_at")
    private LocalDateTime lastLoginAt;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;


    @Builder
    private Member(String name, String phoneNumber, String email, String loginId, String password, Date birth, Grade grade){
        super(name, phoneNumber, email);
        this.loginId = loginId;
        this.password = password;
        this.birth = birth;
        this.createdAt = LocalDateTime.now();
        this.status = MemberStatus.ACTIVE;
        this.grade = grade;
    }

    public Member modifyMember(MemberUpdateRequest memberUpdateRequest) {
        super.name = memberUpdateRequest.name();
        super.phoneNumber = memberUpdateRequest.phoneNumber();
        super.email = memberUpdateRequest.email();
        if (Objects.nonNull(memberUpdateRequest.newPassword())) {
            this.password = memberUpdateRequest.newPassword();
        }
        this.birth = memberUpdateRequest.birth();

        return this;
    }
}
