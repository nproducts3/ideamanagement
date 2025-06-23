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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {
    private final DeploymentRepository deploymentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public DeploymentDto createDeployment(DeploymentDto deploymentDto) {
        Deployment deployment = modelMapper.map(deploymentDto, Deployment.class);
        deployment = deploymentRepository.save(deployment);
        return modelMapper.map(deployment, DeploymentDto.class);
    }

    @Override
    @Transactional
    public DeploymentDto updateDeployment(UUID id, DeploymentDto deploymentDto) {
        Deployment deployment = deploymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Deployment not found with id: " + id));
        
        modelMapper.map(deploymentDto, deployment);
        deployment = deploymentRepository.save(deployment);
        return modelMapper.map(deployment, DeploymentDto.class);
    }

    @Override
    @Transactional
    public void deleteDeployment(UUID id) {
        if (!deploymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Deployment not found with id: " + id);
        }
        deploymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DeploymentDto getDeploymentById(UUID id) {
        Deployment deployment = deploymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Deployment not found with id: " + id));
        return modelMapper.map(deployment, DeploymentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeploymentDto> getAllDeployments(Pageable pageable) {
        return deploymentRepository.findAll(pageable)
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
} 