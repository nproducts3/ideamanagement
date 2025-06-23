package com.ideamanagement.service.impl;

import com.ideamanagement.dto.ProjectDto;
import com.ideamanagement.entity.Project;
import com.ideamanagement.repository.ProjectRepository;
import com.ideamanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        if (projectRepository.existsByName(projectDto.getName())) {
            throw new IllegalArgumentException("Project with this name already exists");
        }

        Project project = new Project();
        project.setName(projectDto.getName());
        project = projectRepository.save(project);

        return convertToDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProject(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return convertToDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public ProjectDto updateProject(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getName().equals(projectDto.getName()) && 
            projectRepository.existsByName(projectDto.getName())) {
            throw new IllegalArgumentException("Project with this name already exists");
        }

        project.setName(projectDto.getName());
        project = projectRepository.save(project);

        return convertToDto(project);
    }

    @Override
    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return projectRepository.existsByName(name);
    }

    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
} 