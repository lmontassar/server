package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    public List<Transaction> findByBuyer(User u);
    public List<Transaction> findBySeller(User u);
}
