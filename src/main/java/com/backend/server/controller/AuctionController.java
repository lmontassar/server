package com.backend.server.controller;

import com.backend.server.entity.Auction;
import com.backend.server.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping("/all")
    public List<Auction> getAuctions() {
        return auctionService.getAuctions();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED) // Set response status to 201
    public Auction addAuction(@RequestBody Auction auction) {
        return auctionService.addAuction(auction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable Long id) {
        Auction auction = auctionService.getAuction(id);
        if (auction != null) {
            return ResponseEntity.ok(auction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/close/{id}")
    public void closeAuction(@PathVariable Long id) {
        auctionService.changeStatus(id, Auction.Status.CLOSED);
    }

    @PutMapping("/open/{id}")
    public void openAuction(@PathVariable Long id) {
        auctionService.changeStatus(id, Auction.Status.OPEN);
    }

    @PutMapping("/cancel/{id}")
    public void cancelAuction(@PathVariable Long id) {
        auctionService.changeStatus(id, Auction.Status.CANCELED);
    }
}
