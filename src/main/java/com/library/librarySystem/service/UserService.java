package com.library.librarySystem.service;

import com.library.librarySystem.model.User;
import com.library.librarySystem.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    public User registerUser(String username, String password) {
//        if (userRepository.findByUsername(username).isPresent()) {
//            throw new IllegalStateException("Username already exists.");
//        }
//
//        User newUser = new User();
//        newUser.setUsername(username);
//        newUser.setPassword(passwordEncoder.encode(password));
//        newUser.setRole(com.library.librarySystem.model.Role.USER);
//        return userRepository.save(newUser);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}