package com.ideamanagement.controller;

import com.ideamanagement.dto.ProjectDto;
import com.ideamanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "APIs for managing projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the provided details")
    public ResponseEntity<ProjectDto> createProject(
            @Parameter(description = "Project details") @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.createProject(projectDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a project by its unique identifier")
    public ResponseEntity<ProjectDto> getProject(
            @Parameter(description = "Project ID") @PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves a paginated list of all projects")
    public ResponseEntity<Page<ProjectDto>> getAllProjects(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project with new details")
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "Project ID") @PathVariable UUID id,
            @Parameter(description = "Updated project details") @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes a project by its ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Project ID") @PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
} 