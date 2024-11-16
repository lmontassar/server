package com.backend.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_seq_gen")
    @SequenceGenerator(name = "auction_seq_gen", sequenceName = "auction_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "participation_price", nullable = false)
    private String participationPrice;
    @Column(name = "start_price", nullable = false)
    private float startPrice;

    @Column(name = "current_price")
    private float currentPrice;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    public enum Status {
        OPEN,
        CLOSED,
        CANCELED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
