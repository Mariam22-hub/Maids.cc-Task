package com.library.librarySystem.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarySystem.config.security.JwtService;
import com.library.librarySystem.dto.LoginUserDto;
import com.library.librarySystem.dto.RegisterUserDto;
import com.library.librarySystem.dto.UserDto;
import com.library.librarySystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testuser");
        registerUserDto.setPassword("password123");

        loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testuser");
        loginUserDto.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        userDto = new UserDto();
        userDto.setUsername("testuser");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(authService.signup(any(RegisterUserDto.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(authService, times(1)).signup(any(RegisterUserDto.class));
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        when(authService.authenticate(any(LoginUserDto.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600));

        verify(authService, times(1)).authenticate(any(LoginUserDto.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }
}