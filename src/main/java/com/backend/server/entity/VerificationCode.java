package com.backend.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_code_seq_gen")
    @SequenceGenerator(name = "verification_code_seq_gen", sequenceName = "verification_code_seq", allocationSize = 1)
    private Long id;
    private String email;
    private String code;
    private LocalDate expiration_date;
}
