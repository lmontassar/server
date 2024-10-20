package com.backend.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Transaction;
@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {

}
