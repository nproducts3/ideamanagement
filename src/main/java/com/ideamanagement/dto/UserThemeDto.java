package com.ideamanagement.dto;

import com.ideamanagement.entity.UserTheme;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserThemeDto {
    private UUID id;
    private UUID userId;
    private UserTheme.ThemeType theme;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID employeeId;
} 