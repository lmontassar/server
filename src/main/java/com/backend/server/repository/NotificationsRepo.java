package com.backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Notifications;
import com.backend.server.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface NotificationsRepo extends JpaRepository<Notifications,Long>{

    @Modifying
    @Transactional
    @Query("UPDATE Notifications n SET n.seen = :seen WHERE n.client = :client")
    int updateSeenByClient(@Param("seen") Boolean seen, @Param("client") User client);
    List<Notifications> findBySeenAndClient(boolean seen,User Client);
    
}