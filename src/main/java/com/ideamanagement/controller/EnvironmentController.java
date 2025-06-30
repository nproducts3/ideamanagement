package com.ideamanagement.controller;

import com.ideamanagement.dto.EnvironmentDto;
import com.ideamanagement.entity.Environment;
import com.ideamanagement.service.EnvironmentService;
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
@RequestMapping("/api/environments")
@RequiredArgsConstructor
@Tag(name = "Environment Management", description = "APIs for managing environments")
// @CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, allowCredentials = "true")
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @PostMapping
    @Operation(summary = "Create a new environment", description = "Creates a new environment with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environment created successfully",
            content = @Content(schema = @Schema(implementation = EnvironmentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<EnvironmentDto> createEnvironment(
        @Parameter(description = "Environment details", required = true)
        @Valid @RequestBody EnvironmentDto environmentDto) {
        return ResponseEntity.ok(environmentService.createEnvironment(environmentDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an environment", description = "Updates an existing environment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environment updated successfully",
            content = @Content(schema = @Schema(implementation = EnvironmentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Environment not found")
    })
    public ResponseEntity<EnvironmentDto> updateEnvironment(
        @Parameter(description = "ID of the environment to update", required = true)
        @PathVariable UUID id,
        @Parameter(description = "Updated environment details", required = true)
        @Valid @RequestBody EnvironmentDto environmentDto) {
        return ResponseEntity.ok(environmentService.updateEnvironment(id, environmentDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an environment", description = "Deletes an environment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Environment not found")
    })
    public ResponseEntity<Void> deleteEnvironment(
        @Parameter(description = "ID of the environment to delete", required = true)
        @PathVariable UUID id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get environment by ID", description = "Retrieves an environment by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environment retrieved successfully",
            content = @Content(schema = @Schema(implementation = EnvironmentDto.class))),
        @ApiResponse(responseCode = "404", description = "Environment not found")
    })
    public ResponseEntity<EnvironmentDto> getEnvironmentById(
        @Parameter(description = "ID of the environment to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(environmentService.getEnvironmentById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get environment by name", description = "Retrieves an environment by its name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environment retrieved successfully",
            content = @Content(schema = @Schema(implementation = EnvironmentDto.class))),
        @ApiResponse(responseCode = "404", description = "Environment not found")
    })
    public ResponseEntity<EnvironmentDto> getEnvironmentByName(
        @Parameter(description = "Name of the environment to retrieve", required = true)
        @PathVariable String name) {
        return ResponseEntity.ok(environmentService.getEnvironmentByName(name));
    }

    @GetMapping
    @Operation(summary = "Get all environments", description = "Retrieves a list of all environments with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<EnvironmentDto>> getAllEnvironments(
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(environmentService.getAllEnvironments(pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get environments by status", description = "Retrieves environments with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Environments retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<EnvironmentDto>> getEnvironmentsByStatus(
        @Parameter(description = "Environment status", required = true)
        @PathVariable Environment.EnvironmentStatus status,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(environmentService.getEnvironmentsByStatus(status, pageable));
    }
} 