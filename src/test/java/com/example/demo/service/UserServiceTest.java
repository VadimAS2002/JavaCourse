package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldSaveUserAndReturnUser() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        doNothing().when(notificationService).createNotification(any(User.class), any(String.class));

        User result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(notificationService, times(1)).createNotification(savedUser, "User testuser registered successfully!");
    }

    @Test
    void getUserById_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void getUserById_ShouldReturnUserIfExists() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getUserByUsername_ShouldReturnUserIfExists() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getUserByUsername_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserByUsername("nonexistentuser");
        assertFalse(result.isPresent());
    }
}
