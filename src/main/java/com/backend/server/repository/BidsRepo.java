package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Bids;
import com.backend.server.entity.User;
@Repository
public interface BidsRepo extends JpaRepository<Bids,Long> {
    public List<Bids> findByAuction(Auction auction);
    public List<Bids> findByBuyer(User u);
    @Query(value = "SELECT * FROM (SELECT * FROM bids WHERE auction_id = :auction_id ORDER BY amount DESC) WHERE ROWNUM = 1",nativeQuery = true)
    public Bids findByAuctionOrderByAmountDesc(@Param("auction_id")Long auction_id);
}
