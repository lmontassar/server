package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Bids;
import com.backend.server.entity.User;

public interface BidsRepo extends JpaRepository<Bids,Long> {
public List<Bids> findAllByAuction(Auction auction); 
public List<Bids> findAllByUser(User u);
}
