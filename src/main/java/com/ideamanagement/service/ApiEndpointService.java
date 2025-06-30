package com.ideamanagement.service;

import com.ideamanagement.dto.ApiEndpointDto;
import com.ideamanagement.entity.ApiEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApiEndpointService {
    ApiEndpointDto createEndpoint(ApiEndpointDto endpointDto);
    ApiEndpointDto updateEndpoint(UUID id, UUID employeeId, ApiEndpointDto endpointDto);
    void deleteEndpoint(UUID id, UUID employeeId);
    ApiEndpointDto getEndpointById(UUID id, UUID employeeId);
    Page<ApiEndpointDto> getAllEndpoints(UUID employeeId, Pageable pageable);
    Page<ApiEndpointDto> getEndpointsByStatus(ApiEndpoint.Status status, Pageable pageable);
    Page<ApiEndpointDto> getEndpointsByMethod(ApiEndpoint.HttpMethod method, Pageable pageable);
    Page<ApiEndpointDto> getEndpointsByVersion(String version, Pageable pageable);
} 