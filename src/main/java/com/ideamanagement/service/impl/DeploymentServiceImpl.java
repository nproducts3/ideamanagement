package com.ideamanagement.service.impl;

import com.ideamanagement.dto.DeploymentDto;
import com.ideamanagement.entity.Deployment;
import com.ideamanagement.exception.EntityNotFoundException;
import com.ideamanagement.repository.DeploymentRepository;
import com.ideamanagement.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ideamanagement.repository.EmployeeRepository;
import com.ideamanagement.entity.Employee;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {
    private final DeploymentRepository deploymentRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public DeploymentDto createDeployment(DeploymentDto deploymentDto) {
        Deployment deployment = modelMapper.map(deploymentDto, Deployment.class);
        if (deploymentDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(deploymentDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + deploymentDto.getEmployeeId()));
            deployment.setEmployee(employee);
        } else {
            deployment.setEmployee(null);
        }
        deployment = deploymentRepository.save(deployment);
        DeploymentDto result = modelMapper.map(deployment, DeploymentDto.class);
        if (deployment.getEmployee() != null) {
            result.setEmployeeId(deployment.getEmployee().getId());
        }
        return result;
    }

    @Override
    @Transactional
    public DeploymentDto updateDeployment(UUID id, UUID employeeId, DeploymentDto deploymentDto) {
        Deployment deployment = deploymentRepository.findByIdAndEmployeeId(id, employeeId);
        if (deployment == null) {
            throw new EntityNotFoundException("Deployment not found with id: " + id + " for employee: " + employeeId);
        }
        modelMapper.map(deploymentDto, deployment);
        if (deploymentDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(deploymentDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + deploymentDto.getEmployeeId()));
            deployment.setEmployee(employee);
        } else {
            deployment.setEmployee(null);
        }
        deployment = deploymentRepository.save(deployment);
        DeploymentDto result = modelMapper.map(deployment, DeploymentDto.class);
        if (deployment.getEmployee() != null) {
            result.setEmployeeId(deployment.getEmployee().getId());
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteDeployment(UUID id, UUID employeeId) {
        Deployment deployment = deploymentRepository.findByIdAndEmployeeId(id, employeeId);
        if (deployment == null) {
            throw new EntityNotFoundException("Deployment not found with id: " + id + " for employee: " + employeeId);
        }
        deploymentRepository.deleteByIdAndEmployeeId(id, employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public DeploymentDto getDeploymentById(UUID id, UUID employeeId) {
        Deployment deployment = deploymentRepository.findByIdAndEmployeeId(id, employeeId);
        if (deployment == null) {
            throw new EntityNotFoundException("Deployment not found with id: " + id + " for employee: " + employeeId);
        }
        return modelMapper.map(deployment, DeploymentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getAllDeployments(UUID employeeId, Pageable pageable) {
        return deploymentRepository.findByEmployeeId(employeeId, pageable)
            .map(deployment -> modelMapper.map(deployment, DeploymentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getDeploymentsByEnvironment(String environment, Pageable pageable) {
        return deploymentRepository.findByEnvironment(environment, pageable)
            .map(deployment -> modelMapper.map(deployment, DeploymentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getDeploymentsByStatus(Deployment.DeploymentStatus status, Pageable pageable) {
        return deploymentRepository.findByStatus(status, pageable)
            .map(deployment -> modelMapper.map(deployment, DeploymentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getDeploymentsByHealth(Deployment.HealthStatus health, Pageable pageable) {
        return deploymentRepository.findByHealth(health, pageable)
            .map(deployment -> modelMapper.map(deployment, DeploymentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getDeploymentsByVersion(String version, Pageable pageable) {
        return deploymentRepository.findByVersion(version, pageable)
            .map(deployment -> modelMapper.map(deployment, DeploymentDto.class));
    }

    @Override
    @Transactional
    public DeploymentDto patchDeployment(UUID id, UUID employeeId, DeploymentDto deploymentDto) {
        Deployment deployment = deploymentRepository.findByIdAndEmployeeId(id, employeeId);
        if (deployment == null) {
            throw new EntityNotFoundException("Deployment not found with id: " + id + " for employee: " + employeeId);
        }
        // Only update non-null fields
        if (deploymentDto.getName() != null) deployment.setName(deploymentDto.getName());
        if (deploymentDto.getEnvironment() != null) deployment.setEnvironment(deploymentDto.getEnvironment());
        if (deploymentDto.getStatus() != null) deployment.setStatus(deploymentDto.getStatus());
        if (deploymentDto.getVersion() != null) deployment.setVersion(deploymentDto.getVersion());
        if (deploymentDto.getDeployedAt() != null) deployment.setDeployedAt(deploymentDto.getDeployedAt());
        if (deploymentDto.getBranch() != null) deployment.setBranch(deploymentDto.getBranch());
        if (deploymentDto.getCommitHash() != null) deployment.setCommitHash(deploymentDto.getCommitHash());
        if (deploymentDto.getHealth() != null) deployment.setHealth(deploymentDto.getHealth());
        if (deploymentDto.getProgress() != null) deployment.setProgress(deploymentDto.getProgress());
        if (deploymentDto.getCreatedAt() != null) deployment.setCreatedAt(deploymentDto.getCreatedAt());
        if (deploymentDto.getUpdatedAt() != null) deployment.setUpdatedAt(deploymentDto.getUpdatedAt());
        if (deploymentDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(deploymentDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + deploymentDto.getEmployeeId()));
            deployment.setEmployee(employee);
        }
        deployment = deploymentRepository.save(deployment);
        DeploymentDto result = modelMapper.map(deployment, DeploymentDto.class);
        if (deployment.getEmployee() != null) {
            result.setEmployeeId(deployment.getEmployee().getId());
        }
        return result;
    }
} 