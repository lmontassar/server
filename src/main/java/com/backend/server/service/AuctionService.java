package com.backend.server.service;

import com.backend.server.entity.Auction;
import com.backend.server.repository.AuctionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    private final AuctionRepo auctionRepo;

    @Autowired
    public AuctionService(AuctionRepo auctionRepo) {
        this.auctionRepo = auctionRepo;
    }
    @Scheduled(fixedRate = 60000) // Check every minute
    public void closeExpiredAuctions() {
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        // Convert LocalDateTime to Date
        Date nowDate = Date.from(nowLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Auction> auctionsToClose = auctionRepo.findByEndTimeBeforeAndStatus(nowDate, Auction.Status.OPEN);

        for (Auction auction : auctionsToClose) {
            auction.setStatus(Auction.Status.CLOSED);
            auctionRepo.save(auction);
        }
    }
    public List<Auction> getAuctions() {
        return auctionRepo.findAll();
    }

    public Auction getAuction(Long id) {
        // Use Optional to handle the case where the auction might not be found
        return auctionRepo.findById(id).orElse(null);
    }

    public Auction updateAmount(Auction auction)
    {
        return auctionRepo.save(auction);
    }

    public Auction addAuction(Auction auction) {
        // If using Oracle, ensure your entity has proper ID generation strategy
        return auctionRepo.save(auction);
    }
    public List<Auction> findAll(){
        return auctionRepo.findAll();
    }
    public void changeStatus(Long id, Auction.Status status) {
        // Fetch the auction first
        Optional<Auction> auctionOptional = auctionRepo.findById(id);
        if (auctionOptional.isPresent()) {
            Auction auction = auctionOptional.get();
            auction.setStatus(status);
            auctionRepo.save(auction); // Save the updated auction
        } else {
            throw new IllegalArgumentException("Auction with id " + id + " not found.");
        }
    }
    public List<Auction> getAuctionsByUser(Long userid){
        return auctionRepo.findAuctionsBySellerId(userid);
    }
}
