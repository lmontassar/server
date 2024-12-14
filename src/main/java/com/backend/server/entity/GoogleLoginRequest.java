package com.backend.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoogleLoginRequest {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String imageUrl;
}
