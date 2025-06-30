package com.ideamanagement.controller;

import com.ideamanagement.dto.DeploymentDto;
import com.ideamanagement.entity.Deployment;
import com.ideamanagement.service.DeploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
@Tag(name = "Deployment Management", description = "APIs for managing deployments")
// @CrossOrigin(origins = "*", allowCredentials = "true")
public class DeploymentController {
    private final DeploymentService deploymentService;

    @PostMapping
    @Operation(summary = "Create a new deployment", description = "Creates a new deployment with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployment created successfully",
            content = @Content(schema = @Schema(implementation = DeploymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<DeploymentDto> createDeployment(
        @RequestParam UUID employeeId,
        @Parameter(description = "Deployment details", required = true)
        @Valid @RequestBody DeploymentDto deploymentDto) {
        deploymentDto.setEmployeeId(employeeId);
        return ResponseEntity.ok(deploymentService.createDeployment(deploymentDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a deployment", description = "Updates an existing deployment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployment updated successfully",
            content = @Content(schema = @Schema(implementation = DeploymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Deployment not found")
    })
    public ResponseEntity<DeploymentDto> updateDeployment(
        @PathVariable UUID id,
        @RequestParam UUID employeeId,
        @Parameter(description = "Updated deployment details", required = true)
        @Valid @RequestBody DeploymentDto deploymentDto) {
        return ResponseEntity.ok(deploymentService.updateDeployment(id, employeeId, deploymentDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a deployment", description = "Deletes a deployment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Deployment not found")
    })
    public ResponseEntity<Void> deleteDeployment(
        @PathVariable UUID id,
        @RequestParam UUID employeeId) {
        deploymentService.deleteDeployment(id, employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deployment by ID", description = "Retrieves a deployment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployment retrieved successfully",
            content = @Content(schema = @Schema(implementation = DeploymentDto.class))),
        @ApiResponse(responseCode = "404", description = "Deployment not found")
    })
    public ResponseEntity<DeploymentDto> getDeploymentById(
        @PathVariable UUID id,
        @RequestParam UUID employeeId) {
        return ResponseEntity.ok(deploymentService.getDeploymentById(id, employeeId));
    }

    @GetMapping
    @Operation(summary = "Get all deployments", description = "Retrieves a list of all deployments with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<DeploymentDto>> getAllDeployments(
        @RequestParam UUID employeeId,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(deploymentService.getAllDeployments(employeeId, pageable));
    }

    @GetMapping("/environment/{environment}")
    @Operation(summary = "Get deployments by environment", description = "Retrieves deployments for a specific environment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<DeploymentDto>> getDeploymentsByEnvironment(
        @Parameter(description = "Environment name", required = true)
        @PathVariable String environment,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(deploymentService.getDeploymentsByEnvironment(environment, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get deployments by status", description = "Retrieves deployments with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<DeploymentDto>> getDeploymentsByStatus(
        @Parameter(description = "Deployment status", required = true)
        @PathVariable Deployment.DeploymentStatus status,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(deploymentService.getDeploymentsByStatus(status, pageable));
    }

    @GetMapping("/health/{health}")
    @Operation(summary = "Get deployments by health status", description = "Retrieves deployments with a specific health status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<DeploymentDto>> getDeploymentsByHealth(
        @Parameter(description = "Health status", required = true)
        @PathVariable Deployment.HealthStatus health,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(deploymentService.getDeploymentsByHealth(health, pageable));
    }

    @GetMapping("/version/{version}")
    @Operation(summary = "Get deployments by version", description = "Retrieves deployments with a specific version")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deployments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<DeploymentDto>> getDeploymentsByVersion(
        @Parameter(description = "Version number", required = true)
        @PathVariable String version,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(deploymentService.getDeploymentsByVersion(version, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeploymentDto> patchDeployment(
        @PathVariable UUID id,
        @RequestParam UUID employeeId,
        @RequestBody DeploymentDto deploymentDto) {
        return ResponseEntity.ok(deploymentService.patchDeployment(id, employeeId, deploymentDto));
    }
} 