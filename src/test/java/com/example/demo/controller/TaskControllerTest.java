package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getPendingTasks_ShouldReturnListOfPendingTasksWithStatusOK() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setCompleted(false);

        when(taskService.getPendingTasks()).thenReturn(Arrays.asList(task1, task2));

        ResponseEntity<List<Task>> response = taskController.getPendingTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllTasksByUserId_ShouldReturnListOfTasksForGivenUserIdWithStatusOK() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setCompleted(false);

        when(taskService.getAllTasksByUserId(1L)).thenReturn(Arrays.asList(task1, task2));

        ResponseEntity<List<Task>> response = taskController.getAllTasksByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createTask_ShouldCreateTaskAndReturnCreatedTaskWithStatusCreated() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setCompleted(false);

        when(taskService.createTask(any(Task.class), any(Long.class))).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void createTask_ShouldReturnBadRequestIfTaskCreationFailed() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setCompleted(false);

        when(taskService.createTask(any(Task.class), any(Long.class))).thenReturn(null);

        ResponseEntity<Task> response = taskController.createTask(task, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteTask_ShouldDeleteTaskAndReturnNoContent() {
        Long taskId = 1L;

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(taskId);
    }
}
