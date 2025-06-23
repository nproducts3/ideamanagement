package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ideamanagement.entity.Idea.Priority;
import com.ideamanagement.entity.Idea.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdeaDto {
    private UUID id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String assignedTo;
    private int upvotes;
    private int comments;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private LocalDateTime createdAt;
    private Set<String> tags;
} 