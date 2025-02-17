package com.library.librarySystem.auth;

import com.library.librarySystem.dto.LoginUserDto;
import com.library.librarySystem.dto.RegisterUserDto;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.model.Role;
import com.library.librarySystem.model.User;
import com.library.librarySystem.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testuser");
        registerUserDto.setPassword("password123");
        registerUserDto.setRole(Role.USER);

        loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testuser");
        loginUserDto.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword123");
        user.setRole(Role.USER);
    }

    @Test
    void testSignup_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = authService.signup(registerUserDto);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals(Role.USER, registeredUser.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void testSignup_UsernameAlreadyExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEntryException.class, () -> authService.signup(registerUserDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticate_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        doAnswer(invocation -> null).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        User authenticatedUser = authService.authenticate(loginUserDto);

        assertNotNull(authenticatedUser);
        assertEquals("testuser", authenticatedUser.getUsername());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authService.authenticate(loginUserDto));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}