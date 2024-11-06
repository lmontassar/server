package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Payment;
import com.backend.server.entity.User;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public List<Payment> findByUser(User user);
}
