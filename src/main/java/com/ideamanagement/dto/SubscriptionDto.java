package com.ideamanagement.dto;

import com.ideamanagement.entity.Subscription;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionDto {
    private UUID id;
    private UUID userId;
    private Subscription.PlanType plan;
    private Subscription.StatusType status;
    private LocalDate startedAt;
    private LocalDate expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 