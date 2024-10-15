package com.backend.server.service;

import java.util.ArrayList;
import java.util.List;

import com.backend.server.repository.AuctionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.User;
import com.backend.server.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public User findById(Long id){
        return repo.findById(id).orElse(null);
    }
    public List<User> findAll(){
        return repo.findAll();
    }
}
