package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ideamanagement.entity.ApiEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiEndpointDto {
    private UUID id;
    private String name;
    private ApiEndpoint.HttpMethod method;
    private String path;
    private ApiEndpoint.Status status;
    private String version;
    private LocalDate lastTested;
    private Integer responseTimeMs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 