package com.nhnacademy.heukbaekbookshop.memberset.address.domain;

import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private void setMember(Member member) {
        this.member = member;
        member.getMemberAddresses().add(this);
    }

    @Builder
    public MemberAddress(Member member,Long postalCode, String roadNameAddress, String detailAddress, String alias) {
        this.setMember(member);
        this.postalCode = postalCode;
        this.roadNameAddress = roadNameAddress;
        this.detailAddress = detailAddress;
        this.alias = alias;
        this.createdAt = LocalDateTime.now();
    }

    public MemberAddress modifyMemberAddress(MemberAddressRequest memberAddressRequest) {
        this.postalCode = memberAddressRequest.postalCode();
        this.roadNameAddress = memberAddressRequest.roadNameAddress();
        this.detailAddress = memberAddressRequest.detailAddress();
        this.alias = memberAddressRequest.alias();
        return this;
    }

}
