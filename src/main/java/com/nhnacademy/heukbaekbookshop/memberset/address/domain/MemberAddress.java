package com.nhnacademy.heukbaekbookshop.memberset.address.domain;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members_addresses")
public class MemberAddress {

    @Id
    @Column(name = "member_address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member member;

    @NotNull
    @Column(name = "postal_code")
    private Long postalCode;

    @NotNull
    @Column(name = "road_name_address")
    private String roadNameAddress;

    @NotNull
    @Column(name = "detail_address")
    private String detailAddress;

    @NotNull
    @Column(name = "alias")
    private String alias;

    @NotNull
    @Column(name = "address_created_at")
    private LocalDateTime createdAt;

    @Builder
    public MemberAddress(Member member,Long postalCode, String roadNameAddress, String detailAddress, String alias) {
        this.member = member;
        this.postalCode = postalCode;
        this.roadNameAddress = roadNameAddress;
        this.detailAddress = detailAddress;
        this.alias = alias;
        this.createdAt = LocalDateTime.now();
    }

    public MemberAddress modifyMemberAddress(MemberAddressDto memberAddressDto) {
        this.postalCode = memberAddressDto.postalCode();
        this.roadNameAddress = memberAddressDto.roadNameAddress();
        this.detailAddress = memberAddressDto.detailAddress();
        this.alias = memberAddressDto.alias();
        return this;
    }

}
