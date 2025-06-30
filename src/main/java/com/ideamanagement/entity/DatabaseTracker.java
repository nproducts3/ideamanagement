package com.ideamanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "database_tracker")
public class DatabaseTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, length = 20)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.created;

    @Column(name = "last_modified", nullable = false)
    private LocalDate lastModified;

    @Column(name = "tables_count", nullable = false)
    private Integer tablesCount = 0;

    @Column(name = "migrations_count", nullable = false)
    private Integer migrationsCount = 0;

    @Column(name = "migrations_json", nullable = false, columnDefinition = "TEXT")
    private String migrationsJson = "[]";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_dbtracker_employee", foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE"))
    private Employee employee;

    public enum Status {
        approved, pending, created, failed
    }
} 