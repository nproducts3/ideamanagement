package com.ideamanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "deployments")
public class Deployment {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String environment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeploymentStatus status;

    @Column(nullable = false, length = 20)
    private String version;

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;

    @Column(nullable = false, length = 100)
    private String branch;

    @Column(name = "commit_hash", nullable = false, length = 50)
    private String commitHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthStatus health = HealthStatus.UNKNOWN;

    @Column(length = 3)
    private String progress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_deployment_employee", foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE"))
    private Employee employee;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum DeploymentStatus {
        PENDING, DEPLOYING, DEPLOYED, FAILED
    }

    public enum HealthStatus {
        HEALTHY, UNKNOWN, UNHEALTHY
    }
} 