package com.backend.server.service;

import java.time.LocalDateTime;
import java.util.List;

import com.backend.server.repository.UserRepo;
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
    @Autowired
    AuctionService aucSer;
    @Autowired
    UserService usrSer;

    @Autowired
    UserRepo usrRepo;

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

    public Bids addBid(Bids bid ) throws RuntimeException {
        Auction auction = aucSer.getAuction(bid.getAuction().getId());
        if (auction == null || auction.getStatus()== Auction.Status.CLOSED) {
            throw new RuntimeException("Auction is not valid or has ended.");
        }
        User buyer = usrSer.findById(bid.getBuyer().getId());
        if (bid.getAmount() > buyer.getAmount()) {
            throw new RuntimeException("Your credit is low !");
        }
        buyer.setAmount((float) (buyer.getAmount() - bid.getAmount()));
        List<Bids> bidsList = bidsRepo.findByAuction(auction);

        if(!bidsList.isEmpty()){
            //Return user amount
            
            Bids lastBid = bidsRepo.findByAuctionOrderByAmountDesc(auction.getId());
            if(lastBid.getAmount() > bid.getAmount()){
                throw new RuntimeException("the amount is lower then the minimum");
            }
            if(lastBid.getBuyer().getId().equals(buyer.getId()) ){
                throw new RuntimeException("You can't add a bid !");
            }
            User lastBuyer = usrSer.findById(lastBid.getBuyer().getId());
            lastBuyer.setAmount((float) (lastBuyer.getAmount()+lastBid.getAmount()));
            usrRepo.save(lastBuyer);
            //
        }
        //send notification to the last buyer
        Bids b = new Bids();
        b.setBuyer(buyer);
        b.setAuction(auction);
        b.setAmount(bid.getAmount());
        b.setTime(LocalDateTime.now());
        auction.setCurrentPrice((float) bid.getAmount());
        aucSer.updateAmount(auction);
        return bidsRepo.save(b);
    }

    public List<Bids> getAllBidsByAuction(Auction a){
        return bidsRepo.findByAuction(a);
    }

    public List<Bids> getAllBidsByUser(User u){
        return bidsRepo.findByBuyer(u);
    }

    public void DeleteBid(Bids b){
        bidsRepo.delete(b);
    }


}
