package com.backend.server.service;
import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
import com.backend.server.repository.AuctionRepo;
import com.backend.server.repository.BidsRepo;
import com.backend.server.repository.TransactionRepo;
import com.backend.server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransporterService {
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidsRepo bidsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TransactionRepo transRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userservice;


    @Scheduled(cron = "0 55 23 * * ?")
    public void closeDayTransaction() {
        try {
            List<User> transporters = userRepo.findAllByRole(User.Role.TRANSPORTER);
            for (User transporter : transporters) {
                List<Transaction> todayDeliveredTransactions = transRepo.getTransactionByTransporterAndTodayDate(transporter.getId());
                double totalEarnings = transporter.getAmount();
                for (Transaction trans : todayDeliveredTransactions) {
                    totalEarnings += trans.getTransporter_price(); // Assuming transporter_price is `transporterPrice` in camelCase
                }
                transporter.setAmount((float) totalEarnings); // Cast back to float if necessary
                userRepo.save(transporter);
                if(totalEarnings>0)
                {
                    emailService.sendEmail(
                            transporter.getEmail(),
                            "Daily Salary Payment Confirmation",
                            "<p>Dear " + transporter.getFirstname() + ",</p>"
                                    + "<p>We are pleased to inform you that your daily salary has been processed successfully.</p>"
                                    + "<p>Total Amount Credited: <strong>" + totalEarnings + " TND</strong></p>"
                                    + "<p>Thank you for your service and for using our platform.</p>"
                                    + "<p>Best regards,<br/>S&D Team</p>"
                    );
                }
            }
        }catch(Exception e){
            System.out.println("Error : "+e.getMessage());
        }
    }

    public List<User> getTransporters(){
        return userRepo.findAllByRole(User.Role.TRANSPORTER);
    }

    public User getTransporter(Long id){
        return (User)userRepo.findAllByRoleAndId(User.Role.TRANSPORTER,id);
    }

}
