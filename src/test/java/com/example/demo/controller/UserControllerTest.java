package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
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
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    void registerUser_ShouldCreateNewUser() {
        User user = new User();
        user.setUsername("testUser2");
        user.setPassword("password2");

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("testUser2", response.getBody().getUsername());
    }

    @Test
    void login_ShouldReturnUserWhenUsernameExists() {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password3");
        userService.registerUser(user);

        ResponseEntity<Optional<User>> response = userController.login("existingUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals("existingUser", response.getBody().get().getUsername());
    }
}
