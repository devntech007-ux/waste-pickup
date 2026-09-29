package com.wastepickup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "household")
@Getter @Setter @NoArgsConstructor
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String address;

    /** Average of the last N pickup scores; null until the first pickup. */
    private Double rollingAverage;

    /** true when rollingAverage is below the configured minimum. */
    private boolean flagged = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
