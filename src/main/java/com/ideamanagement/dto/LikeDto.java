package com.ideamanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LikeDto {
    private UUID id;
    private UUID userId;
    private UUID ideaId;
    private LocalDateTime createdAt;
} 