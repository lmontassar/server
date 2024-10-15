package com.backend.server.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Bids;
import com.backend.server.entity.User;
import com.backend.server.service.AuctionService;
import com.backend.server.service.BidsService;
import com.backend.server.service.UserService;



@RestController
@RequestMapping(path="/bid")
public class BidsController {
    @Autowired
    BidsService bidService;
    @Autowired
    AuctionService aucSer;
    @Autowired
    UserService usrSer;

 

    
    @GetMapping("/bids")
    public ResponseEntity<?> getAllBids(){
        try {
            List<Bids> l=bidService.getBids();
            return ResponseEntity.accepted().body(l);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
            
    }

    @GetMapping("/bid/{id}")
    public ResponseEntity<?> getBidById(@PathVariable("id") Long id) {
        try {
            Bids b=bidService.getBid(id);
            return ResponseEntity.accepted().body(b);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bid/add")
    public ResponseEntity<?> addBid(@RequestBody Bids b) {
        try {
            return ResponseEntity.accepted().body(b);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/bidsByAuction/{id}")
    public ResponseEntity<?> getBidsByAuction(@PathVariable("id") Long id) {
        try {
            
            Auction a=aucSer.getAuction(id);
            List<Bids> l=bidService.getAllBidsByAuction(a);
            return ResponseEntity.accepted().body(l);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/bidsByUser/{id}")
    public ResponseEntity<?> getBidsByUser(@PathVariable("id") Long id) {
        try {
            
            User u=usrSer.findById(id);
            List<Bids> l=bidService.getAllBidsByUser(u);
            return ResponseEntity.accepted().body(l);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/bid/{id}")
    public ResponseEntity<?> deleteBid(@PathVariable("id") Long id){
        try {
            Bids b=bidService.getBid(id);
            bidService.DeleteBid(b);
            return ResponseEntity.accepted().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    
    


}
