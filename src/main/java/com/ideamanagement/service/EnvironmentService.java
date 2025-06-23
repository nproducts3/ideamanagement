package com.ideamanagement.service;

import com.ideamanagement.dto.EnvironmentDto;
import com.ideamanagement.entity.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnvironmentService {
    EnvironmentDto createEnvironment(EnvironmentDto environmentDto);
    EnvironmentDto updateEnvironment(UUID id, EnvironmentDto environmentDto);
    void deleteEnvironment(UUID id);
    EnvironmentDto getEnvironmentById(UUID id);
    EnvironmentDto getEnvironmentByName(String name);
    Page<EnvironmentDto> getAllEnvironments(Pageable pageable);
    Page<EnvironmentDto> getEnvironmentsByStatus(Environment.EnvironmentStatus status, Pageable pageable);
} 