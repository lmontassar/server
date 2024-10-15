package com.backend.server.controller;


import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path="/auction")
public class AuctionController {
    @Autowired
    AuctionService auctionService;

    @GetMapping("/auctions")
    public List<Auction> getAuctions(){
            return auctionService.getAuctions();
    }

    @GetMapping("/auction/{id}")
    public Auction getAuction(@PathVariable Long id){
        return  auctionService.getAuction(id);
    }

    @PostMapping("/auction/add")
    public Auction addAuction(@RequestBody Auction auction){
        return  auctionService.addAuction(auction);
    }

    @PutMapping("/auction/close/{id}")
    public void closeAuction(@PathVariable Long id)throws AccountNotFoundException{
        auctionService.changeStatus(id,Auction.Status.CLOSED);
    }
    @PutMapping("/auction/open/{id}")
    public void openAuction(@PathVariable Long id)throws AccountNotFoundException{
        auctionService.changeStatus(id,Auction.Status.OPEN);
    }
    @PutMapping("/auction/cancel/{id}")
    public void cancelAuction(@PathVariable Long id)throws AccountNotFoundException{
        auctionService.changeStatus(id,Auction.Status.CANCELED);
    }



}
