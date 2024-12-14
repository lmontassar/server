package com.backend.server.service;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Bids;
import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
import com.backend.server.entity.Transaction.Status;
import com.backend.server.repository.AuctionRepo;
import com.backend.server.repository.BidsRepo;
import com.backend.server.repository.TransactionRepo;

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
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidsRepo bidsRepo;
    @Autowired
    private TransactionRepo transRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userservice;
   
    @Scheduled(fixedRate = 5000) // Check every minute
    public void closeExpiredAuctions() {
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        // Convert LocalDateTime to Date
        Date nowDate = Date.from(nowLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Auction> auctionsToClose = auctionRepo.findByEndTimeBeforeAndStatus(nowDate, Auction.Status.OPEN);

        for ( Auction auction : auctionsToClose ) {
            auction.setStatus(Auction.Status.CLOSED); // closed the auction
            auctionRepo.save(auction);

            // send mail to seller
           try{

               List<Bids> bidsList = bidsRepo.findByAuctionOrderByAmountDesc(auction.getId());
               int Bought = 0;
               for (Bids b:bidsList) {
                   User buyer = b.getBuyer();
                   if(Bought == 1){
                       buyer.setAmount(buyer.getAmount()+auction.getParticipationPrice());
                       emailService.sendEmail(
                               buyer.getEmail(),
                               "Bid Lost",
                               "<p>Dear " + buyer.getFirstname() + ",</p>"
                                       + "<p>We would like to inform you that you lost the bid : </p>"
                                       + "<p><strong>Auction Title:</strong> " + auction.getTitle() + "</p>"
                                       + "<p>You will receive the participation amount ASAP .</p>"
                                       + "<p>Thank you for using our platform. Please feel free to check your account for the final bid details or any further actions.</p>"
                                       + "<p>Best regards,<br/>S&D Team</p>"
                       );
                   }
                   if(b.getAmount()>buyer.getAmount() && Bought == 0){
                       emailService.sendEmail(
                               buyer.getEmail(),
                               "Bid Lost",
                               "<p>Dear " + buyer.getFirstname() + ",</p>"
                                       + "<p>We would like to inform you that you lost the bid : </p>"
                                       + "<p><strong>Auction Title:</strong> " + auction.getTitle() + "</p>"
                                       + "<p>You will not receive the participation amount because you don't have the needed amount to pay , i Hope you understand </p>"
                                       + "<p>Thank you for using our platform. Please feel free to check your account for the final bid details or any further actions.</p>"
                                       + "<p>Best regards,<br/>S&D Team</p>"
                       );
                   }else{
                       if(Bought == 0){
                           User seller = auction.getSeller();
                           buyer.setAmount(buyer.getAmount()+auction.getParticipationPrice());
                           buyer.setAmount((float)(buyer.getAmount() - b.getAmount()));

                           Transaction transaction = new Transaction();
                           transaction.setCreation_date(new Date());
                           transaction.setBuyer(buyer);
                           transaction.setTransporter_price(auction.getWeight()*0.85);
                           transaction.setStatus(Status.NotStarted);
                           transaction.setAuction(auction);
                           transaction.setSeller(auction.getSeller());
                           transaction.setTransaction_date(new Date());
                           transaction.setAmount(b.getAmount());
                           transRepo.save(transaction);
                           userservice.save(seller);
                           userservice.save(buyer);
                           emailService.sendEmail(
                                   buyer.getEmail(),
                                   "Congratulations! You've Won the Auction",
                                   "<p>Dear " + buyer.getFirstname() + ",</p>"
                                           + "<p>We’re excited to inform you that the auction has ended, and you are the winning bidder!</p>"
                                           + "<p><strong>Auction ID:</strong> " + auction.getId() + "</p>"
                                           + "<p>Please log in to your account to review the auction details and complete any remaining steps for the transaction.</p>"
                                           + "<p>Thank you for participating, and congratulations on your win!</p>"
                                           + "<p>Best regards,<br/>S&D Team</p>"
                           );
                           emailService.sendEmail(
                                   auction.getSeller().getEmail(),
                                   "Auction Ended Notification",
                                   "<p>Dear " + auction.getSeller().getFirstname() + ",</p>"
                                           + "<p>We would like to inform you that your auction has ended, bought by "+buyer.getFirstname()+" "+buyer.getLastname()+".</p>"
                                           + "<p><strong>Auction ID:</strong> " + auction.getId() + "</p>"
                                           + "<p>Thank you for using our platform. Please feel free to check your account for the final bid details or any further actions.</p>"
                                           + "<p>Best regards,<br/>S&D Team</p>"
                           );
                           Bought = 1;
                       }
                   }
               }
               if(Bought == 0){
                   emailService.sendEmail(
                           auction.getSeller().getEmail(),
                           "Auction Canceled",
                           "<p>Dear " + auction.getSeller().getFirstname() + ",</p>"
                                   + "<p>Sorry to inform you that your auction has been canceled due to no one bought it before the end time : </p>"
                                   + "<p><strong>Auction ID:</strong> " + auction.getId() + "</p>"
                                   + "<p><strong>Auction Title:</strong> " + auction.getTitle() + "</p>"
                                   + "<p>You can created the auction again or you can extend the time of the auction.</p>"
                                   + "<p>Thank you for using our platform. Please feel free to check your account for the final bid details or any further actions.</p>"
                                   + "<p>Best regards,<br/>S&D Team</p>"
                   );
               }
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
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
