package com.example.demo.controller;

import com.example.demo.model.Notification;
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
    private UserService userService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
        notificationController = new NotificationController(notificationService);
    }

    @Test
    void getAllNotificationsForUser_ReturnsNotifications() {
        Long userId = 1L;
        Notification notification1 = new Notification();
        notification1.setUserId(userId);
        notification1.setMessage("Notification 1");
        notificationService.createNotification(userId, "Notification 1");

        Notification notification2 = new Notification();
        notification2.setUserId(userId);
        notification2.setMessage("Notification 2");
        notificationService.createNotification(userId, "Notification 2");

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsForUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Notification 1", response.getBody().get(0).getMessage());
        assertEquals("Notification 2", response.getBody().get(1).getMessage());
    }

    @Test
    void getPendingNotificationsForUser_ReturnsPendingNotifications() {
        notificationService.createNotification(1L, "Pending Notification");

        notificationService.createNotification(1L, "Pending Notification");

        ResponseEntity<List<Notification>> response = notificationController.getPendingNotificationsForUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Pending Notification", response.getBody().get(0).getMessage());
    }
}
