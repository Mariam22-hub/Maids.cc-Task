package com.library.librarySystem.respository;

import com.library.librarySystem.model.Role;
import com.library.librarySystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Test
    void testFindByUsername_UserExists() {
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

        assertFalse(foundUser.isPresent());
    }
}