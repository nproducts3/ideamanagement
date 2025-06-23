package com.ideamanagement.dto;

import com.ideamanagement.entity.IntegrationSettings;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class IntegrationSettingsDto {
    private UUID id;
    private UUID userId;
    private IntegrationSettings.IntegrationType type;
    private boolean connected;
    private String config;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 