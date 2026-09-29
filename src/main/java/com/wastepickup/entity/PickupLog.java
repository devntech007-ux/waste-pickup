package com.wastepickup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_log")
@Getter @Setter @NoArgsConstructor
public class PickupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String collectorName;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private LocalDateTime pickupTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "household_id")
    private Household household;
}
