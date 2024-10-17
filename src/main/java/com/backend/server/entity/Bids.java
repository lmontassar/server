package com.backend.server.entity;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bids {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid_id;
    @ManyToOne
    private Auction auction;
    @ManyToOne
    private User buyer;
    private double bid_amount;
    private DateTimeFormat bid_time; 
}
