package com.ideamanagement.dto;

import com.ideamanagement.entity.DatabaseTracker.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DatabaseTrackerDto {
    private Integer id;
    private String name;
    private String version;
    private Status status;
    private LocalDate lastModified;
    private Integer tablesCount;
    private Integer migrationsCount;
    private String migrationsJson;
    private UUID employeeId;
} 