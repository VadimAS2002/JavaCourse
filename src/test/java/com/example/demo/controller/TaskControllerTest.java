package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.NotificationService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskController taskController;

    @Autowired
    private NotificationService notificationService;

    private final LocalDateTime now = LocalDateTime.now();

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, userService, notificationService);
        taskController = new TaskController(taskService);
    }

    @Test
    void getPendingTasks_ReturnsPendingTasks() {
        User user = new User();
        user.setUsername("testUser10");
        user.setPassword("password10");
        User registeredUser = userService.registerUser(user);

        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, registeredUser);

        ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate, registeredUser.getId());
        ResponseEntity<List<Task>> responseEntity2 = taskController.getPendingTasks();

        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertFalse(responseEntity2.getBody().get(0).isCompleted());
        assertFalse(responseEntity2.getBody().get(0).isDeleted());
    }

    @Test
    public void getTasksByUserId_ReturnsOk() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        User user = new User();
        user.setUsername("testUser11");
        user.setPassword("password11");
        User registeredUser = userService.registerUser(user);
        tasks.add(new Task(1L, "Task 1", "Description", now, now.plusDays(1), false,
                false, user));

        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasksByUserId(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void createTask_ValidTask_ReturnsCreated() {
        User user = new User();
        user.setUsername("testUser12");
        user.setPassword("password12");
        User registeredUser = userService.registerUser(user);
        Task taskToCreate = new Task(null, "Valid Task", "Description",
                null, null, false, false, registeredUser);

        ResponseEntity<Task> responseEntity = taskController.createTask(taskToCreate, registeredUser.getId());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteTask_ExistingTask_ReturnsNoContent() {
        User user = new User();
        user.setUsername("testUser18");
        user.setPassword("password18");
        User registeredUser = userService.registerUser(user);

        Task task = new Task(null, "Valid Task", "Description", null,
                null, false, false, registeredUser);
        ResponseEntity<Task> responseEntity = taskController.createTask(task, registeredUser.getId());
        ResponseEntity<Void> responseEntity2 = taskController.deleteTask(task.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity2.getStatusCode());
    }
}
