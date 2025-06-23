package com.ideamanagement.service.impl;

import com.ideamanagement.dto.EnvironmentDto;
import com.ideamanagement.entity.Environment;
import com.ideamanagement.exception.EntityNotFoundException;
import com.ideamanagement.repository.EnvironmentRepository;
import com.ideamanagement.service.EnvironmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {
    private final EnvironmentRepository environmentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public EnvironmentDto createEnvironment(EnvironmentDto environmentDto) {
        Environment environment = modelMapper.map(environmentDto, Environment.class);
        environment = environmentRepository.save(environment);
        return modelMapper.map(environment, EnvironmentDto.class);
    }

    @Override
    @Transactional
    public EnvironmentDto updateEnvironment(UUID id, EnvironmentDto environmentDto) {
        Environment environment = environmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Environment not found with id: " + id));
        
        modelMapper.map(environmentDto, environment);
        environment = environmentRepository.save(environment);
        return modelMapper.map(environment, EnvironmentDto.class);
    }

    @Override
    @Transactional
    public void deleteEnvironment(UUID id) {
        if (!environmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Environment not found with id: " + id);
        }
        environmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EnvironmentDto getEnvironmentById(UUID id) {
        Environment environment = environmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Environment not found with id: " + id));
        return modelMapper.map(environment, EnvironmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EnvironmentDto getEnvironmentByName(String name) {
        Environment environment = environmentRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Environment not found with name: " + name));
        return modelMapper.map(environment, EnvironmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnvironmentDto> getAllEnvironments(Pageable pageable) {
        return environmentRepository.findAll(pageable)
            .map(environment -> modelMapper.map(environment, EnvironmentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnvironmentDto> getEnvironmentsByStatus(Environment.EnvironmentStatus status, Pageable pageable) {
        return environmentRepository.findByStatus(status, pageable)
            .map(environment -> modelMapper.map(environment, EnvironmentDto.class));
    }
} 