package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ideamanagement.entity.ApiEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiTestLogDto {
    private UUID id;
    private UUID endpointId;
    private ApiEndpoint.HttpMethod requestMethod;
    private String requestPath;
    private String requestBody;
    private String responseBody;
    private LocalDateTime executedAt;
} 