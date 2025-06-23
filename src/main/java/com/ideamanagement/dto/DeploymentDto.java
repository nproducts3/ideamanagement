package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideamanagement.entity.Deployment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeploymentDto {
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Environment is required")
    @Size(max = 50, message = "Environment must be less than 50 characters")
    private String environment;

    @NotNull(message = "Status is required")
    private Deployment.DeploymentStatus status;

    @NotBlank(message = "Version is required")
    @Size(max = 20, message = "Version must be less than 20 characters")
    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deployedAt;

    @NotBlank(message = "Branch is required")
    @Size(max = 100, message = "Branch must be less than 100 characters")
    private String branch;

    @NotBlank(message = "Commit hash is required")
    @Size(max = 50, message = "Commit hash must be less than 50 characters")
    private String commitHash;

    @NotNull(message = "Health status is required")
    private Deployment.HealthStatus health;

    @Pattern(regexp = "^[0-9]{1,3}$", message = "Progress must be a number between 0 and 999")
    private String progress;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
} 