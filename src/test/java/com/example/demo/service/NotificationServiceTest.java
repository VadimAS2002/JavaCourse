package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void getAllNotificationsForUser_ShouldReturnAndMarkAsReadNotificationsForUser() {
        User user = new User();
        user.setId(1L);

        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUser(user);
        notification1.setMessage("Notification 1");
        notification1.setRead(false);
        notification1.setTimeStamp(LocalDateTime.now());

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUser(user);
        notification2.setMessage("Notification 2");
        notification2.setRead(false);
        notification2.setTimeStamp(LocalDateTime.now());

        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> notifications = notificationService.getAllNotificationsForUser(1L);

        assertEquals(2, notifications.size());
        assertEquals("Notification 1", notifications.get(0).getMessage());
        assertEquals("Notification 2", notifications.get(1).getMessage());
        assertTrue(notifications.get(0).isRead());
        assertTrue(notifications.get(1).isRead());

        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void getPendingNotifications_ShouldReturnAndMarkAsReadPendingNotifications() {
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

        when(notificationRepository.findByReadFalse()).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> notifications = notificationService.getPendingNotifications();

        assertEquals(2, notifications.size());
        assertEquals("Notification 1", notifications.get(0).getMessage());
        assertEquals("Notification 2", notifications.get(1).getMessage());
        assertTrue(notifications.get(0).isRead());
        assertTrue(notifications.get(1).isRead());

        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void createNotification_ShouldCreateNotification() {
        User user = new User();
        user.setId(1L);
        String message = "Test Notification";

        notificationService.createNotification(user, message);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
