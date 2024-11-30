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


    @GetMapping("/available/{id}")
    public ResponseEntity<?> getAvailableTransactions(@PathVariable Long id){
        List<Transaction> transactions = transactionService.getAvailable(id);
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

    @PutMapping("/delivered/{id}")
    public Transaction Delivered(@PathVariable Long id) {
        return transactionService.changeStatus(id, Transaction.Status.DELIVERED);
    }
    @PutMapping("/{tid}/started/{id}")
    public ResponseEntity<?> started(@PathVariable Long tid, @PathVariable Long id) {
        try {
            // Call the affectTransaction service method and receive the updated transaction
            Transaction updatedTransaction = transactionService.affectTransaction(tid, id);

            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            // Return a 404 Not Found status if the transaction or user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Return a 500 Internal Server Error status for any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Transaction> cancel(@PathVariable Long id) {
        try {
            // Call the affectTransaction service method and receive the updated transaction
            Transaction updatedTransaction = transactionService.cancelTransaction(id);

            // Return the updated transaction in the response with a 200 OK status
            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            // Return a 404 Not Found status if the transaction or user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Return a 500 Internal Server Error status for any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    

}
