package com.backend.server.repository;

import com.backend.server.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepo extends JpaRepository<VerificationCode,Long> {
    public Optional<VerificationCode> findByCode(String code);
    public VerificationCode getByEmail(String email);
    public int deleteAllByCode(String code);
}
