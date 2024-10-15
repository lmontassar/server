package com.backend.server.controller;


import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.entity.Auction;
import com.backend.server.service.AuctionService;


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
        this.auctionService.changeStatus(id,Auction.Status.CLOSED);
    }



}
