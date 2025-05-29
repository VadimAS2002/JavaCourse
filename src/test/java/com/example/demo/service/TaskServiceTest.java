package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void getPendingTasks_ShouldReturnPendingTasks() {
        User user1 = new User();
        user1.setUsername("testUser4");
        user1.setPassword("password4");
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCreationDate(LocalDateTime.now());
        task1.setCompleted(false);
        task1.setDeleted(false);
        task1.setUser(user1);
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("testUser5");
        user2.setPassword("password5");
        user2 = userRepository.save(user2);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCreationDate(LocalDateTime.now());
        task2.setCompleted(true);
        task2.setDeleted(false);
        task2.setUser(user2);

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> pendingTasks = taskService.getPendingTasks();

        assertEquals(1, pendingTasks.size());
        assertEquals("Task 1", pendingTasks.get(0).getTitle());
    }

    @Test
    void getAllTasksByUserId_ShouldReturnTasksForUser() {
        User user1 = new User();
        user1.setUsername("testUser6");
        user1.setPassword("password6");

        User user2 = new User();
        user2.setUsername("testUser7");
        user2.setPassword("password7");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCreationDate(LocalDateTime.now());
        task1.setTargetDate(LocalDateTime.now().plusDays(1));
        task1.setCompleted(false);
        task1.setDeleted(false);
        task1.setUser(user1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCreationDate(LocalDateTime.now());
        task2.setTargetDate(LocalDateTime.now().plusDays(2));
        task2.setCompleted(false);
        task2.setDeleted(false);
        task2.setUser(user1);

        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setDescription("Description 3");
        task3.setCreationDate(LocalDateTime.now());
        task3.setTargetDate(LocalDateTime.now().plusDays(3));
        task3.setCompleted(false);
        task3.setDeleted(false);
        task3.setUser(user2);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        List<Task> tasks = taskService.getAllTasksByUserId(user1.getId());

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    @Test
    void createTask_ShouldSaveTask() {
        User user = new User();
        user.setUsername("testUser8");
        user.setPassword("password8");

        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("Task 5");
        task.setDescription("Description 5");
        task.setCreationDate(LocalDateTime.now());
        task.setTargetDate(LocalDateTime.now().plusDays(1));
        task.setCompleted(false);
        task.setDeleted(false);
        task.setUser(user);

        Task savedTask = taskService.createTask(task, user.getId());

        assertNotNull(savedTask.getId());
        assertEquals("Task 5", savedTask.getTitle());
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        User user = new User();
        user.setUsername("testUser9");
        user.setPassword("password9");

        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("Task 6");
        task.setDescription("Description 6");
        task.setCreationDate(LocalDateTime.now());
        task.setTargetDate(LocalDateTime.now().plusDays(1));
        task.setCompleted(false);
        task.setDeleted(false);
        task.setUser(user);
        Task savedTask = taskRepository.save(task);

        taskService.deleteTask(savedTask.getId());

        assertFalse(taskRepository.findById(savedTask.getId()).isPresent());
    }
}
