package com.backend.server.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String email;
    private String password;
}
