package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_ShouldSaveUser() {
        User testUser = new User("testuser", "password");
        User savedUser = userService.registerUser(testUser);

        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void getUserByUsername_ShouldReturnUserIfFound() {
        User testUser = new User("testuser1", "password1");
        userRepository.save(testUser);

        Optional<User> user = userService.getUserByUsername("testuser1");

        assertTrue(user.isPresent());
        assertEquals("testuser1", user.get().getUsername());
    }

    @Test
    void getUserByUsername_ShouldReturnEmptyOptionalIfNotFound() {
        Optional<User> user = userService.getUserByUsername("nonexistentuser");

        assertFalse(user.isPresent());
    }
}
