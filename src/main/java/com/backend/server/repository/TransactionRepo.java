package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Transaction;
import com.backend.server.entity.User;
@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    public List<Transaction> findByBuyer(User u);
    public List<Transaction> findBySeller(User u);
    @Query("SELECT t FROM Transaction t WHERE t.transporter.id = :tid AND FUNCTION('DATE', t.transaction_date) = CURRENT_DATE and t.status=com.backend.server.entity.Transaction.Status.DELIVERED")
    public List<Transaction> getTransactionByTransporterAndTodayDate(Long tid);
    public List<Transaction> findTransactionsByStatus(Transaction.Status status);
    public List<Transaction> findTransactionsByTransporter(User u);
    @Query("SELECT t FROM Transaction t WHERE t.transporter.id = :id OR t.status = com.backend.server.entity.Transaction.Status.NotStarted")
    public List<Transaction> getAvailable(Long id);

}
