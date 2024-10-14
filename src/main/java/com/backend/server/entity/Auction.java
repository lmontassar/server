package com.backend.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Long seller_id;
    private String title;
    private String discription;
    private float start_price;
    private float current_price;
    private Date start_time;
    private Date end_time;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;
}
