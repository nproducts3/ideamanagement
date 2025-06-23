package com.ideamanagement.service;

import com.ideamanagement.dto.ProjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    ProjectDto getProject(UUID id);
    Page<ProjectDto> getAllProjects(Pageable pageable);
    ProjectDto updateProject(UUID id, ProjectDto projectDto);
    void deleteProject(UUID id);
    boolean existsByName(String name);
} 