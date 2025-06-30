package com.ideamanagement.service;

import com.ideamanagement.dto.DeploymentDto;
import com.ideamanagement.entity.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeploymentService {
    DeploymentDto createDeployment(DeploymentDto deploymentDto);
    DeploymentDto updateDeployment(UUID id, UUID employeeId, DeploymentDto deploymentDto);
    void deleteDeployment(UUID id, UUID employeeId);
    DeploymentDto getDeploymentById(UUID id, UUID employeeId);
    Page<DeploymentDto> getAllDeployments(UUID employeeId, Pageable pageable);
    Page<DeploymentDto> getDeploymentsByEnvironment(String environment, Pageable pageable);
    Page<DeploymentDto> getDeploymentsByStatus(Deployment.DeploymentStatus status, Pageable pageable);
    Page<DeploymentDto> getDeploymentsByHealth(Deployment.HealthStatus health, Pageable pageable);
    Page<DeploymentDto> getDeploymentsByVersion(String version, Pageable pageable);
    DeploymentDto patchDeployment(UUID id, UUID employeeId, DeploymentDto deploymentDto);
} 