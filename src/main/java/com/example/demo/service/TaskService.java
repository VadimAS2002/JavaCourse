package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.TaskRepository;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public List<Task> getPendingTasks() {
        return taskRepository.findByCompletedFalseAndDeletedFalse();
    }

    @Cacheable(value = "tasksByUserId", key = "#userId")
    public List<Task> getAllTasksByUserId(Long userId) {
            return taskRepository.findByUserId(userId);
    }

    @Caching(
            put = { @CachePut(value = "tasks", key = "#result.id") },
            evict = { @CacheEvict(value = "tasksByUserId", key = "#userId") }
    )
    public Task createTask(Task task, Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        User user = userOptional.orElse(null);
        if (user == null) {
            return null;
        }
        task.setUser(user);
        task.setCreationDate(LocalDateTime.now());
        Task createdTask = taskRepository.save(task);
        notificationService.createNotification(user, "Task " + createdTask.getTitle() + " created successfully!");
        return createdTask;
    }

    @CacheEvict(value = "tasksByUserId", key = "#userId")
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            Optional<User> userOptional = userService.getUserById(task.getUser().getId());
            User user = userOptional.orElse(null);

            if (user != null) {
                taskRepository.deleteById(id);
                notificationService.createNotification(user, "Task " + task.getTitle() + " deleted successfully!");
            }
        }
    }
}
