package com.library.librarySystem.auth;

import com.library.librarySystem.config.security.JwtService;
import com.library.librarySystem.dto.LoginUserDto;
import com.library.librarySystem.dto.RegisterUserDto;
import com.library.librarySystem.dto.UserDto;
import com.library.librarySystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public AuthController(JwtService jwtService, ModelMapper modelMapper, AuthService authService) {
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authService.signup(registerUserDto);

        return ResponseEntity.ok(modelMapper.map(registeredUser, UserDto.class));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}