package com.backend.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.entity.User;
import com.backend.server.service.UserService;

@RestController
@RequestMapping(path="api/user")
public class UserController {
    @Autowired
    UserService userSer;
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOneById(Long id){
        try{
            User u = userSer.findById(id) ;
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<?> getOneById(){
        try{
            User u = userSer.findAll();
            return ResponseEntity.accepted().body(u);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
