package com.ideamanagement.service.impl;

import com.ideamanagement.dto.ApiEndpointDto;
import com.ideamanagement.entity.ApiEndpoint;
import com.ideamanagement.repository.ApiEndpointRepository;
import com.ideamanagement.service.ApiEndpointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiEndpointServiceImpl implements ApiEndpointService {
    private final ApiEndpointRepository apiEndpointRepository;

    @Override
    @Transactional
    public ApiEndpointDto createEndpoint(ApiEndpointDto endpointDto) {
        ApiEndpoint endpoint = new ApiEndpoint();
        copyFromDto(endpoint, endpointDto);
        ApiEndpoint savedEndpoint = apiEndpointRepository.save(endpoint);
        return copyToDto(savedEndpoint);
    }

    @Override
    @Transactional
    public ApiEndpointDto updateEndpoint(UUID id, UUID employeeId, ApiEndpointDto endpointDto) {
        ApiEndpoint endpoint = apiEndpointRepository.findByIdAndEmployeeId(id, employeeId);
        if (endpoint == null) {
            throw new EntityNotFoundException("API endpoint not found with id: " + id + " for employee: " + employeeId);
        }
        copyFromDto(endpoint, endpointDto);
        ApiEndpoint updatedEndpoint = apiEndpointRepository.save(endpoint);
        return copyToDto(updatedEndpoint);
    }

    @Override
    @Transactional
    public void deleteEndpoint(UUID id, UUID employeeId) {
        ApiEndpoint endpoint = apiEndpointRepository.findByIdAndEmployeeId(id, employeeId);
        if (endpoint == null) {
            throw new EntityNotFoundException("API endpoint not found with id: " + id + " for employee: " + employeeId);
        }
        apiEndpointRepository.deleteByIdAndEmployeeId(id, employeeId);
    }

    @Override
    public ApiEndpointDto getEndpointById(UUID id, UUID employeeId) {
        ApiEndpoint endpoint = apiEndpointRepository.findByIdAndEmployeeId(id, employeeId);
        if (endpoint == null) {
            throw new EntityNotFoundException("API endpoint not found with id: " + id + " for employee: " + employeeId);
        }
        return copyToDto(endpoint);
    }

    @Override
    public Page<ApiEndpointDto> getAllEndpoints(UUID employeeId, Pageable pageable) {
        return apiEndpointRepository.findByEmployeeId(employeeId, pageable).map(this::copyToDto);
    }

    @Override
    public Page<ApiEndpointDto> getEndpointsByStatus(ApiEndpoint.Status status, Pageable pageable) {
        return apiEndpointRepository.findByStatus(status, pageable)
            .map(this::copyToDto);
    }

    @Override
    public Page<ApiEndpointDto> getEndpointsByMethod(ApiEndpoint.HttpMethod method, Pageable pageable) {
        return apiEndpointRepository.findByMethod(method, pageable)
            .map(this::copyToDto);
    }

    @Override
    public Page<ApiEndpointDto> getEndpointsByVersion(String version, Pageable pageable) {
        return apiEndpointRepository.findByVersion(version, pageable)
            .map(this::copyToDto);
    }

    private void copyFromDto(ApiEndpoint endpoint, ApiEndpointDto dto) {
        endpoint.setName(dto.getName());
        endpoint.setMethod(dto.getMethod());
        endpoint.setPath(dto.getPath());
        endpoint.setStatus(dto.getStatus());
        endpoint.setVersion(dto.getVersion());
        endpoint.setLastTested(dto.getLastTested());
        endpoint.setResponseTimeMs(dto.getResponseTimeMs());
        if (dto.getEmployeeId() != null) {
            com.ideamanagement.entity.Employee emp = new com.ideamanagement.entity.Employee();
            emp.setId(dto.getEmployeeId());
            endpoint.setEmployee(emp);
        }
    }

    private ApiEndpointDto copyToDto(ApiEndpoint endpoint) {
        ApiEndpointDto dto = new ApiEndpointDto();
        dto.setId(endpoint.getId());
        dto.setName(endpoint.getName());
        dto.setMethod(endpoint.getMethod());
        dto.setPath(endpoint.getPath());
        dto.setStatus(endpoint.getStatus());
        dto.setVersion(endpoint.getVersion());
        dto.setLastTested(endpoint.getLastTested());
        dto.setResponseTimeMs(endpoint.getResponseTimeMs());
        dto.setCreatedAt(endpoint.getCreatedAt());
        dto.setUpdatedAt(endpoint.getUpdatedAt());
        return dto;
    }
} 