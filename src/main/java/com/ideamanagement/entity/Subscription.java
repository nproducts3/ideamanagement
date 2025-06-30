package com.ideamanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private PlanType plan = PlanType.FREE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusType status = StatusType.INACTIVE;

    @Column(name = "started_at")
    private LocalDate startedAt;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum PlanType {
        FREE, PREMIUM
    }

    public enum StatusType {
        ACTIVE, INACTIVE, PAST_DUE, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 