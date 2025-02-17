package com.library.librarySystem.dto;

import com.library.librarySystem.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private Role role;

}