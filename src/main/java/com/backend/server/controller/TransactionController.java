package com.backend.server.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Transaction;
import com.backend.server.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path="/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all")
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED) // Set response status to 201
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransaction(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/change/{id}")
    public void changeStatus(@PathVariable Long id) {
        transactionService.changeStatus(id, Transaction.Status.DELIVRED);
    }
    

}
