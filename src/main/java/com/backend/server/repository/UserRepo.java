package com.backend.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    // public User findOneByEmail(String email);
    // public user f
}