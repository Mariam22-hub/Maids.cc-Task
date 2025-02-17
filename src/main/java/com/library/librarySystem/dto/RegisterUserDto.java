package com.library.librarySystem.dto;

import com.library.librarySystem.model.Role;
import lombok.Data;

@Data
public class RegisterUserDto {
    private String username;
    private String password;
    private Role role;
}