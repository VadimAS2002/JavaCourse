package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationControllerTest {

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        notificationService = new NotificationService(notificationRepository);
        notificationController = new NotificationController(notificationService);
    }

    @Test
    void getAllNotificationsForUser_ReturnsNotifications() {
        User user = new User();
        user.setUsername("testUser16");
        user.setPassword("password16");
        User registeredUser = userService.registerUser(user);

        notificationService.createNotification(registeredUser, "Notification 1");
        notificationService.createNotification(registeredUser, "Notification 2");

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsForUser(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertTrue(response.getBody().get(0).getMessage().contains("User"));
        assertEquals("Notification 1", response.getBody().get(1).getMessage());
        assertEquals("Notification 2", response.getBody().get(2).getMessage());
    }

    @Test
    void getPendingNotificationsForUser_ReturnsPendingNotifications() {
        User user = new User();
        user.setUsername("testUser17");
        user.setPassword("password17");
        User registeredUser = userService.registerUser(user);

        notificationService.createNotification(registeredUser, "Pending Notification");
        notificationService.createNotification(registeredUser, "Pending Notification");

        ResponseEntity<List<Notification>> response = notificationController.getPendingNotificationsForUser();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).isRead());
        assertTrue(response.getBody().get(1).isRead());
        assertTrue(response.getBody().get(2).isRead());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
