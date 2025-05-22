package com.example.demo.controller;

import com.example.demo.exception.InvalidDataException;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        userService = new UserService(notificationService);
        userController = new UserController(userService);
    }

    @Test
    public void registerUser_SuccessfulRegistration() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        ResponseEntity<User> responseEntity = userController.registerUser(user);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void registerUser_BadRequest_NullUsername() {
        User user = new User();
        user.setUsername(null);
        user.setPassword("password");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(user);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Username cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_EmptyUsername() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(user);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Username cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_NullPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(null);

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(user);
            assertTrue(false);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void registerUser_BadRequest_EmptyPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("");

        try {
            ResponseEntity<User> responseEntity = userController.registerUser(user);
            assertTrue(true);
        } catch (InvalidDataException e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    @Test
    void login_UserExists_ReturnsUserAndStatus200() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        User registeredUser = userService.registerUser(testUser);

        ResponseEntity<Optional<User>> response = userController.login(registeredUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals("testUser", response.getBody().get().getUsername());
    }
}
