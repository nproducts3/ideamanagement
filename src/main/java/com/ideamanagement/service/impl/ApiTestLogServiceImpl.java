package com.ideamanagement.service.impl;

import com.ideamanagement.dto.ApiTestLogDto;
import com.ideamanagement.entity.ApiEndpoint;
import com.ideamanagement.entity.ApiTestLog;
import com.ideamanagement.repository.ApiEndpointRepository;
import com.ideamanagement.repository.ApiTestLogRepository;
import com.ideamanagement.service.ApiTestLogService;
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
public class ApiTestLogServiceImpl implements ApiTestLogService {
    private final ApiTestLogRepository apiTestLogRepository;
    private final ApiEndpointRepository apiEndpointRepository;

    @Override
    @Transactional
    public ApiTestLogDto createTestLog(ApiTestLogDto testLogDto) {
        ApiEndpoint endpoint = apiEndpointRepository.findById(testLogDto.getEndpointId())
            .orElseThrow(() -> new EntityNotFoundException("API endpoint not found with id: " + testLogDto.getEndpointId()));

        ApiTestLog testLog = new ApiTestLog();
        testLog.setEndpoint(endpoint);
        testLog.setRequestMethod(testLogDto.getRequestMethod());
        testLog.setRequestPath(testLogDto.getRequestPath());
        testLog.setRequestBody(testLogDto.getRequestBody());
        testLog.setResponseBody(testLogDto.getResponseBody());

        ApiTestLog savedTestLog = apiTestLogRepository.save(testLog);
        return copyToDto(savedTestLog);
    }

    @Override
    public ApiTestLogDto getTestLogById(UUID id) {
        ApiTestLog testLog = apiTestLogRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("API test log not found with id: " + id));
        return copyToDto(testLog);
    }

    @Override
    public Page<ApiTestLogDto> getTestLogsByEndpointId(UUID endpointId, Pageable pageable) {
        return apiTestLogRepository.findByEndpointId(endpointId, pageable)
            .map(this::copyToDto);
    }

    @Override
    public Page<ApiTestLogDto> getAllTestLogs(Pageable pageable) {
        return apiTestLogRepository.findAll(pageable)
            .map(this::copyToDto);
    }

    private ApiTestLogDto copyToDto(ApiTestLog testLog) {
        ApiTestLogDto dto = new ApiTestLogDto();
        dto.setId(testLog.getId());
        dto.setEndpointId(testLog.getEndpoint().getId());
        dto.setRequestMethod(testLog.getRequestMethod());
        dto.setRequestPath(testLog.getRequestPath());
        dto.setRequestBody(testLog.getRequestBody());
        dto.setResponseBody(testLog.getResponseBody());
        dto.setExecutedAt(testLog.getExecutedAt());
        return dto;
    }
} 