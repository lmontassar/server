package com.backend.server.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
import com.backend.server.service.TransactionService;
import com.backend.server.service.UserService;

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
    @Autowired
    private UserService userService;

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

    @GetMapping("/bought/user/{id}")
    public ResponseEntity<?> getTransactionByBuyer(@PathVariable Long id) {
        User u = userService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionByBuyer(u);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/sold/user/{id}")
    public ResponseEntity<?> getTransactionBySeller(@PathVariable Long id) {
        User u = userService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionBySeller(u);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/get/transporter/{id}")
    public ResponseEntity<?> getTransactionByTransporter(@PathVariable Long id){
        User u = userService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionByTransporter(u);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/available")
    public ResponseEntity<?> getAvailableTransactions(){
        List<Transaction> transactions = transactionService.getTransactionsByStatus(Transaction.Status.NotStarted);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/inprogress")
    public ResponseEntity<?> getInProgressTransactions(){
        List<Transaction> transactions = transactionService.getTransactionsByStatus(Transaction.Status.INPROGRESS);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/delivered")
    public ResponseEntity<?> getDeliveredTransactions(){
        List<Transaction> transactions = transactionService.getTransactionsByStatus(Transaction.Status.DELIVERED);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/change/delivered/{id}")
    public void Delivered(@PathVariable Long id) {
        transactionService.changeStatus(id, Transaction.Status.DELIVERED);
    }
    @PutMapping("/change/started/{id}")
    public void Started(@PathVariable Long id) {
        transactionService.changeStatus(id, Transaction.Status.INPROGRESS);
    }
    

}
