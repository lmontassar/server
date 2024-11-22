package com.backend.server.controller;

import java.util.List;

import com.backend.server.service.TransporterService;
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
@RequestMapping(path="/transporter")
public class TransporterController{
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @Autowired
    private TransporterService transporterService;
    @GetMapping("/all")
    public ResponseEntity<?> getAll()
    {
        try{
            List<User> u = transporterService.getTransporters();
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> BlockTransporter(@PathVariable Long id)
    {
        try{
            User u = userService.UpdateStatus(id, User.Status.BLOCKED);
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/active/{id}")
    public ResponseEntity<?> ActiveTransporter(@PathVariable Long id)
    {
        try{
            User u = userService.UpdateStatus(id, User.Status.ACTIVE);
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            User u = transporterService.getTransporter(id);
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
