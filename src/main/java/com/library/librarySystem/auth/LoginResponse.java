package com.library.librarySystem.auth;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
}