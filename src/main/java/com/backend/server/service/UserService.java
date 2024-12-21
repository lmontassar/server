package com.backend.server.service;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<User> findAllExceptAdmins(){
        List<User.Role> l = new ArrayList<>();
        l.add(User.Role.TRANSPORTER);
        l.add(User.Role.USER);
        return repo.findAllByRoleIn(l);
    }
    public User BlockById(Long id){
        User u = repo.findById(id).orElse(null);

        u.setStatus(User.Status.BLOCKED);
        return repo.save(u);
    }
    public List<User> findAllByRole(User.Role role){
        return repo.findAllByRole(role);
    }
    public User findOrCreateUser(User u) {
        User existingUser = repo.findOneByEmail(u.getEmail()).orElse(null);
        if (existingUser != null) {
            return existingUser;
        } else {
            u.setCreatedAt(LocalDateTime.now());
            u.setRole(User.Role.USER);
            u.setStatus(User.Status.ACTIVE);
            return repo.save(u);
        }
    }
    public User UpdateStatus(Long id,User.Status status){
        User u = repo.findById(id).orElse(null);
        if (u == null) return null;
        u.setStatus(status);
        return repo.save(u);
    }
    public User updateById(Long id,User upUser){
        User u = repo.findById(id).orElse(null);
        if (u == null) return null;
        BeanUtils.copyProperties(upUser, u , getNullPropertyNames(upUser) );
        return repo.save(u);
    }
    public User save(User u){
        return repo.save(u);
    }
    public User findOneByEmail(String email){
        return repo.findOneByEmail(email).orElse(null);
    }
    public User findOneByUsername(String username){
        return repo.findOneByUsername(username);
    }

    private String[] getNullPropertyNames(Object source) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(source.getClass()))
                .map(pd -> pd.getName())
                .filter(name -> {
                    try {
                        return BeanUtils.getPropertyDescriptor(source.getClass(), name).getReadMethod().invoke(source) == null;
                    } catch (Exception e) {
                        return false;
                    }
                }).toArray(String[]::new);
    }
}