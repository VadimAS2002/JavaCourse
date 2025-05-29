package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime creationDate;
    private LocalDateTime targetDate;
    @Column(nullable = false)
    private boolean completed;
    @Column(nullable = false)
    private boolean deleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Task(Long id, String title, String description, LocalDateTime creationDate, LocalDateTime targetDate,
                boolean completed, boolean deleted, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.targetDate = targetDate;
        this.completed = completed;
        this.deleted = deleted;
        this.user = user;
    }

    public Task() {}

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public User getUser() {
        return user;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
