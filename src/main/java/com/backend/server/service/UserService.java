package com.backend.server.service;


import java.util.List;

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
    public User BlockById(Long id){
        User u = repo.findById(id).orElse(null);
        u.setStatus(User.Status.BLOCKED);
        return repo.save(u);
    }
    public User updateById(Long id,User upUser){
        User u = repo.findById(id).orElse(null);
        u.setUsername(upUser.getUsername());
        u.setPassword(upUser.getPassword());
        u.setEmail(upUser.getEmail());
        u.setRole(upUser.getRole());
        u.setFirstname(upUser.getFirstname());
        u.setLastname(upUser.getLastname());
        u.setNumber(upUser.getNumber());
        u.setAddress(upUser.getAddress());
        u.setCreated_at(upUser.getCreated_at());
        u.setStatus(upUser.getStatus());
        u.setImageurl(upUser.getImageurl());
        return repo.save(u);
    }
}
