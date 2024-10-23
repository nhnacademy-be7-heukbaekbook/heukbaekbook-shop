package com.nhnacademy.heukbaekbookshop.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "returns")
public class Return {

    @Id
    @Column(name = "return_id")
    private long id;

    @NotNull
    @Column(name = "reason")
    private String reason;

    @NotNull
    @Column(name = "return_request_at")
    private LocalDateTime returnRequestAt;

    @NotNull
    @Column(name = "return_approved_at")
    private LocalDateTime returnApprovedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus;
}
