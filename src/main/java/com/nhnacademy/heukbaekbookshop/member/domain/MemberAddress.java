package com.nhnacademy.heukbaekbookshop.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    @NotNull
    @Column(name = "postal_code")
    private long postalCode;

    @NotNull
    @Column(name = "road_name_address")
    private String roadNameAddress;

    @NotNull
    @Column(name = "detail_address")
    private String detailAddress;

    @NotNull
    @Column(name = "alias")
    private String alias;

    @Builder
    private MemberAddress(Member member,Long postalCode, String roadNameAddress, String detailAddress, String alias) {
        this.member = member;
        this.postalCode = postalCode;
        this.roadNameAddress = roadNameAddress;
        this.detailAddress = detailAddress;
        this.alias = alias;
    }

}
