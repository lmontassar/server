package com.backend.server.service;

import com.backend.server.entity.Auction;
import com.backend.server.repository.AuctionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuctionService {
    public final AuctionRepo auctionRepo;

    public AuctionService(AuctionRepo auctionRepo) {
        this.auctionRepo = auctionRepo;
    }

    public List<Auction> getAuctions(){
        return  auctionRepo.findAll();
    }

    public Auction getAuction(Long id)
    {
        return auctionRepo.findById(id).orElse(null);
    }

    public Auction addAuction(Auction auction)
    {
        return auctionRepo.save(auction);
    }
    public void changeStatus(Long id , Auction.Status status){
        Auction auction = getAuction(id);
        auction.setStatus(status);
    }

}
