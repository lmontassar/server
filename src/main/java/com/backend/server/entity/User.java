package com.backend.server.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    public enum Role {
        ADMIN,SELLER,BUYER
    }
    private Role role;
    private String firstname;
    private String lastname;
    private int number;
    private String address;
    private Date created_at;
    public enum Status {
        ACTIVE,BLOCKED
    }
    private Status status;
    private String imageurl;
}
