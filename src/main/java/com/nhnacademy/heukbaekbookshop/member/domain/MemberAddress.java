package com.nhnacademy.heukbaekbookshop.member.domain;

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
@Table(name = "members_addresses")
public class MemberAddress {

    @Id
    @Column(name = "member_address_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    @NotNull
    @Column(name = "postal_code")
    private long postalCode;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "road_name_address")
    private String roadNameAddress;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "detail_address")
    private String detailAddress;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "alias")
    private String alias;

}
