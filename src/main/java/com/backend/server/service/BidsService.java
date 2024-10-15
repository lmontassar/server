package com.backend.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Bids;
import com.backend.server.entity.User;
import com.backend.server.repository.BidsRepo;

@Service
public class BidsService {
    @Autowired
     private BidsRepo bidsRepo;

    public BidsService(BidsRepo bidsRepo) {
        this.bidsRepo = bidsRepo;
    }

    public List<Bids> getBids(){
        return  bidsRepo.findAll();
    }

    public Bids getBid(Long id)
    {
        return bidsRepo.findById(id).orElse(null);
    }

    public Bids addBid(Bids bid)
    {
        return bidsRepo.save(bid);
    }

    public List<Bids> getAllBidsByAuction(Auction a){
        return bidsRepo.findAllByAuction(a);
    }

    public List<Bids> getAllBidsByUser(User u){
        return bidsRepo.findAllByUser(u);
    }

    public void DeleteBid(Bids b){
        
        bidsRepo.delete(b);
    }
    


}
