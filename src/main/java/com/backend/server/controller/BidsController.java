package com.backend.server.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.backend.server.service.EmailService;
import com.backend.server.service.UserService;



@RestController
@RequestMapping(path="/bids")
public class BidsController {
    @Autowired
    BidsService bidService;
    @Autowired
    AuctionService aucSer;
    @Autowired
    UserService usrSer;
    @Autowired
    EmailService emailService;
    @Autowired
    AuctionService auctionService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllBids(){
        try {
            List<Bids> l=bidService.getBids();
            return ResponseEntity.accepted().body(l);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
            
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBidById(@PathVariable("id") Long id) {
        try {
            Bids b=bidService.getBid(id);
            return ResponseEntity.accepted().body(b);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBid(@RequestBody Bids bid) {
        try {

            Bids b = bidService.addBid(bid);
            Auction auction = auctionService.getAuction((b.getAuction()).getId());
            emailService.sendEmail(
                auction.getSeller().getEmail(),
                "New Bid Received on Your Auction",
                "<p>Dear " + auction.getSeller().getFirstname() + ",</p>"
                + "<p>We are pleased to inform you that a new bid has been placed on your auction!</p>"
                + "<p><strong>Auction ID:</strong> " + bid.getAuction().getId() + "</p>"
                + "<p>Visit your account to view the latest bid details and stay updated on the progress of your auction.</p>"
                + "<p>Thank you for using our platform, and best of luck with your auction!</p>"
                + "<p>Warm regards,<br/>The S&D Team</p>"
            );
            return ResponseEntity.accepted().body(b);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/auction/{id}")
    public ResponseEntity<?> getBidsByAuction(@PathVariable("id") Long id) {
        try {
            
            Auction a=aucSer.getAuction(id);
            List<Bids> l=bidService.getAllBidsByAuction(a);
            return ResponseEntity.accepted().body(l);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getBidsByUser(@PathVariable("id") Long id) {
        try {
            
            User u=usrSer.findById(id);
            List<Bids> l=bidService.getAllBidsByUser(u);
            return ResponseEntity.accepted().body(l);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
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
