package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @Test
    void getAllNotificationsForUser_ShouldReturnNotifications() {
        User user = new User();
        user.setUsername("testUser13");
        user.setPassword("password13");
        User registeredUser = userService.registerUser(user);

        Notification notification1 = new Notification(null, "Notification 3", registeredUser, false,
                LocalDateTime.now());
        Notification notification2 = new Notification(null, "Notification 4", registeredUser, true,
                LocalDateTime.now());
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        List<Notification> notifications = notificationService.getAllNotificationsForUser(registeredUser.getId());

        assertEquals(3, notifications.size());
        assertTrue(notifications.get(0).getMessage().contains("User"));
        assertEquals("Notification 3", notifications.get(1).getMessage());
        assertEquals("Notification 4", notifications.get(2).getMessage());
    }

    @Test
    void getPendingNotifications_ShouldReturnPendingNotifications() {
        User user = new User();
        user.setUsername("testUser14");
        user.setPassword("password14");
        User registeredUser = userService.registerUser(user);

        Notification notification1 = new Notification(null, "Notification 1", registeredUser, false,
                LocalDateTime.now());
        Notification notification2 = new Notification(null, "Notification 2", registeredUser, true,
                LocalDateTime.now());
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        List<Notification> pendingNotifications = notificationService.getPendingNotifications();

        assertEquals(2, pendingNotifications.size());
        assertTrue(pendingNotifications.get(0).getMessage().contains("User"));
        assertEquals("Notification 1", pendingNotifications.get(1).getMessage());
    }

    @Test
    void createNotification_ShouldSaveNotification() {
        User user = new User();
        user.setUsername("testUser15");
        user.setPassword("password15");
        User registeredUser = userService.registerUser(user);
        notificationService.createNotification(registeredUser, "New Notification Message");

        List<Notification> notifications = notificationRepository.findByUserId(registeredUser.getId());

        assertEquals("New Notification Message", notifications.get(1).getMessage());
    }
}
