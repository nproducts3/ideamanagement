package com.ideamanagement.service;

import com.ideamanagement.dto.ApiTestLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApiTestLogService {
    ApiTestLogDto createTestLog(ApiTestLogDto testLogDto);
    ApiTestLogDto getTestLogById(UUID id);
    Page<ApiTestLogDto> getTestLogsByEndpointId(UUID endpointId, Pageable pageable);
    Page<ApiTestLogDto> getAllTestLogs(Pageable pageable);
} 