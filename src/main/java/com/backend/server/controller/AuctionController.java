package com.backend.server.controller;

import com.backend.server.entity.Auction;
import com.backend.server.entity.AuctionStatus;
import com.backend.server.service.AuctionService;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path="api/auction")
public class AuctionController {
    public final AuctionService auctionService;

    public AuctionController() {
        this(null);
    }

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/auctions")
    public List<Auction> getAuctions(){
            return this.auctionService.getAuctions();
    }

    @GetMapping("/auction/{id}")
    public Auction getAuction(@PathVariable Long id){
        return  this.auctionService.getAuction(id);
    }

    @PostMapping("/auction/add")
    public Auction addAuction(@RequestBody Auction auction){
        return  this.auctionService.addAuction(auction);
    }

    @PutMapping("/auction/close/{id}")
    public void closeAuction(@PathVariable Long id)throws AccountNotFoundException{
        this.auctionService.changeStatus(id,AuctionStatus.CLOSED);
    }



}
