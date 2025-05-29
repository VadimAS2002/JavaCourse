package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void getAllNotificationsForUser_ShouldReturnNotificationsWithStatusOK() {
        User user = new User();
        user.setId(1L);

        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUser(user);
        notification1.setMessage("Notification 1");
        notification1.setRead(true);
        notification1.setTimeStamp(LocalDateTime.now());

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUser(user);
        notification2.setMessage("Notification 2");
        notification2.setRead(true);
        notification2.setTimeStamp(LocalDateTime.now());

        when(notificationService.getAllNotificationsForUser(1L)).thenReturn(Arrays.asList(notification1, notification2));

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsForUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Notification 1", response.getBody().get(0).getMessage());
        assertEquals("Notification 2", response.getBody().get(1).getMessage());
    }

    @Test
    void getPendingNotificationsForUser_ShouldReturnPendingNotificationsWithStatusOK() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setMessage("Notification 1");
        notification1.setRead(false);
        notification1.setTimeStamp(LocalDateTime.now());

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setMessage("Notification 2");
        notification2.setRead(false);
        notification2.setTimeStamp(LocalDateTime.now());

        when(notificationService.getPendingNotifications()).thenReturn(Arrays.asList(notification1, notification2));

        ResponseEntity<List<Notification>> response = notificationController.getPendingNotificationsForUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Notification 1", response.getBody().get(0).getMessage());
        assertEquals("Notification 2", response.getBody().get(1).getMessage());
    }
}
