package com.ideamanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LikeDto {
    private UUID id;
    private UUID userId;
    private UUID ideaId;
    private UUID employeeId;
    private LocalDateTime createdAt;
} 