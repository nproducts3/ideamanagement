package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideamanagement.entity.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EnvironmentDto {
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotNull(message = "Status is required")
    private Environment.EnvironmentStatus status;

    @Pattern(regexp = "^[0-9]{1,5}$", message = "Deployments count must be a number between 0 and 99999")
    private String deploymentsCount;

    @NotBlank(message = "Last update is required")
    @Size(max = 50, message = "Last update must be less than 50 characters")
    private String lastUpdate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private UUID employeeId;
} 