package com.backend.server.service;

import java.util.Date;
import java.util.List;

import java.util.Optional;

import com.backend.server.repository.UserRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
import com.backend.server.repository.TransactionRepo;

@Service
public class TransactionService {

    private final TransactionRepo tranRepo;
    @Autowired
    public TransactionService(TransactionRepo tranRepo) {
        this.tranRepo = tranRepo;
    }
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailService emailService;

    public List<Transaction> getTransactionByBuyer(User u) {
        return tranRepo.findByBuyer(u);
    }

    public List<Transaction> getTransactionBySeller(User u) {
        return tranRepo.findBySeller(u);
    }

    public List<Transaction> getTransactions(){
        return tranRepo.findAll();
    }

    public Transaction getTransaction(Long id){
        return tranRepo.findById(id).orElse(null);
    }

    public Transaction addTransaction(Transaction transaction){
        return tranRepo.save(transaction);
    }

    public Transaction changeStatus(Long id, Transaction.Status status)  {
        // Fetch the auction first
        Optional<Transaction> transactionOptional = tranRepo.findById(id);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            User seller = transaction.getSeller();
            User buyer = transaction.getBuyer();
            seller.setAmount((float)(seller.getAmount() + transaction.getAmount()));
            transaction.setStatus(status);
            try {
                emailService.sendEmail(
                        buyer.getEmail(),
                        "Congratulations! You've Won the Auction",
                        "<p>Dear " + buyer.getFirstname() + ",</p>"
                                + "<p>We’re thrilled to let you know that your package has arrived! \uD83C\uDF81</p>"
                                + "<p>Here are the details of your delivery:</p>"
                                + "<p><strong>Order Number:</strong> " + transaction.getId() + "</p>"
                                + "<p><strong>Transporter Name:</strong> " + transaction.getTransporter().getFirstname()+" "+transaction.getTransporter().getLastname() + "</p>"
                                + "<p><strong>Delivered On:</strong> " + new Date() + "</p>"
                                + "<p><strong>Delivery Address:</strong> " + transaction.getAuction().getId() + "</p>"
                                + "<p><strong>Product ID:</strong> " + transaction.getAuction().getId() + "</p>"
                                + "<p>Thank you for choosing [Your Company Name]. If you have any questions or concerns regarding your order, please don’t hesitate to contact us at [Support Email or Phone Number].\n" +
                                "\n" +
                                "We hope you enjoy your purchase!\n" +
                                "\n" +
                                "Warm regards,.</p>"
                                + "<p>S&D Team</p>"
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return tranRepo.save(transaction); // Save the updated auction
        } else {
            throw new IllegalArgumentException("Transaction with id " + id + " not found.");
        }
    }
    public Transaction cancelTransaction(Long tranId) {
        Optional<Transaction> tr = tranRepo.findById(tranId);

        if (tr.isPresent()) {
            Transaction t = tr.get();

            // Set transporter and update status

            t.setStatus(Transaction.Status.NotStarted);
            t.setTransporter(null);
            // Save and return the updated transaction
            return tranRepo.save(t);
        } else {
            throw new IllegalArgumentException("Transaction with id " + tranId );
        }
    }
    public Transaction affectTransaction(Long tid, Long tranId) {
        User user = userRepo.getUserById(tranId);
        Transaction tr = tranRepo.getTransactionById(tid);
        if (user!=null && tr!=null) {
            // Set transporter and update status

            tr.setTransporter(user);
            tr.setStatus(Transaction.Status.INPROGRESS);

            // Save and return the updated transaction
            return tranRepo.save(tr);
        } else {
            throw new IllegalArgumentException("Transaction with id " + tranId + " or User with id " + tid + " not found.");
        }
    }

    public List<Transaction> getTransactionByTransporter(User u){
        return tranRepo.findTransactionsByTransporter(u);
    }
    public List<Transaction> getAvailable(Long u){
        return tranRepo.getAvailable(u);
    }
    public List<Transaction> getTransactionsByStatus(Transaction.Status s){
        return tranRepo.findTransactionsByStatus(s);
    }

    public void DeleteTransaction(Transaction transaction){
        tranRepo.delete(transaction);
    }
}
