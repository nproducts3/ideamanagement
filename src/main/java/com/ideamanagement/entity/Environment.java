package com.ideamanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "environments")
public class Environment {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnvironmentStatus status = EnvironmentStatus.ACTIVE;

    @Column(name = "deployments_count", nullable = false, length = 5)
    private String deploymentsCount = "0";

    @Column(name = "last_update", nullable = false, length = 50)
    private String lastUpdate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_environment_employee", foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE"))
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

    public enum EnvironmentStatus {
        ACTIVE, MAINTENANCE
    }
} 