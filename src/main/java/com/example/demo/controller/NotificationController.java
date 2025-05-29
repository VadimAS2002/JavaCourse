package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getAllNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotificationsForUser() {
        List<Notification> notifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(notifications);
    }
}
