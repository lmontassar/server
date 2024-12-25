package com.backend.server.controller;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Notifications;
import com.backend.server.entity.User;
import com.backend.server.service.AuctionService;
import com.backend.server.service.NotificationsService;
import com.backend.server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // For ResponseEntity

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;

@RestController
@RequestMapping(path = "/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private NotificationsService nService;

    @Autowired
    private UserService uService;

    @GetMapping("/get/notifications/user/{id}")
    public ResponseEntity<?> getNotifications(@PathVariable Long id){
        try{
            User Client = uService.findById(id);
            List<Notifications> n = nService.GetNotificationByUser(Client);
            List<String> s = new ArrayList<>();
            for (Notifications i : n) {
                s.add(i.getMessage());
            }
            return ResponseEntity.accepted().body(s);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        try{
            List<Auction> auctions = auctionService.findAll() ;
            return ResponseEntity.accepted().body(auctions);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAuctionsByUser(@PathVariable Long id){
        try{
            List<Auction> auctions = auctionService.getAuctionsByUser(id) ;
            return ResponseEntity.accepted().body(auctions);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
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
