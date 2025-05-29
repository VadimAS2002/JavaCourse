package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void registerUser_ShouldCreateNewUserAndReturnCreatedStatus() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(userService.registerUser(any(User.class))).thenReturn(savedUser);

        ResponseEntity<User> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    void registerUser_ShouldReturnInternalServerErrorIfRegistrationFails() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void login_ShouldReturnUserIfExists() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        ResponseEntity<Optional<User>> response = userController.login("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(testUser, response.getBody().get());
    }

    @Test
    void login_ShouldReturnNotFoundIfUserDoesNotExist() {
        when(userService.getUserByUsername("nonexistentuser")).thenReturn(Optional.empty());

        ResponseEntity<Optional<User>> response = userController.login("nonexistentuser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
