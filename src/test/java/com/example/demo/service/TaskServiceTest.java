package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getPendingTasks_ShouldReturnListOfPendingTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1");
        task1.setCompleted(false);

        when(taskRepository.findByCompletedFalseAndDeletedFalse()).thenReturn(Arrays.asList(task1));

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(1, pendingTasks.size());
        assertEquals("Task 1", pendingTasks.get(0).getDescription());
    }

    @Test
    void getAllTasksByUserId_ShouldReturnListOfTasksForGivenUserId() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2");
        task2.setCompleted(true);

        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasksByUserId(1L);

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getDescription());
        assertEquals("Task 2", tasks.get(1).getDescription());
    }

    @Test
    void createTask_ShouldCreateTaskAndReturnCreatedTask() {
        Task task = new Task();
        task.setDescription("New Task");
        task.setCompleted(false);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setCompleted(false);
        savedTask.setUser(user);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        doNothing().when(notificationService).createNotification(any(User.class), any(String.class));

        Task createdTask = taskService.createTask(task, 1L);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        assertEquals(user, createdTask.getUser());
        verify(notificationService, times(1)).createNotification(user, "Task New Task created successfully!");
    }

    @Test
    void createTask_ShouldReturnNullIfUserDoesNotExist() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setCompleted(false);

        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        Task createdTask = taskService.createTask(task, 1L);

        assertNull(createdTask);
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Existing Task");
        User user = new User();
        user.setId(1L);
        task.setUser(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        doNothing().when(notificationService).createNotification(any(User.class), any(String.class));

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
        verify(notificationService, times(1)).createNotification(user, "Task Existing Task deleted successfully!");
    }
}
