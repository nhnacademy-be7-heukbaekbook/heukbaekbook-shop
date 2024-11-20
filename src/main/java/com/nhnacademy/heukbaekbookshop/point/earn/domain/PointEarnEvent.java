package com.nhnacademy.heukbaekbookshop.point.earn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "points_earns_events")
public class PointEarnEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_earn_event_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_code", unique = true)
    private EventCode eventCode;

    @OneToMany(mappedBy = "pointEarnEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PointEarnStandard> pointEarnStandards;
}
