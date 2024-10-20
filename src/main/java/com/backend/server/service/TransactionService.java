package com.backend.server.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Transaction;
import com.backend.server.repository.TransactionRepo;

@Service
public class TransactionService {

    private final TransactionRepo tranRepo;
    @Autowired
    public TransactionService(TransactionRepo tranRepo) {
        this.tranRepo = tranRepo;
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

        public void changeStatus(Long id, Transaction.Status status) {
        // Fetch the auction first
        Optional<Transaction> transactionOptional = tranRepo.findById(id);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            transaction.setStatus(status);
            tranRepo.save(transaction); // Save the updated auction
        } else {
            throw new IllegalArgumentException("Transaction with id " + id + " not found.");

    
        }

    }
    public void DeleteTransaction(Transaction transaction){
        tranRepo.delete(transaction);
    }
}
