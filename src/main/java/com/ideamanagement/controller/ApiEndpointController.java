package com.ideamanagement.controller;

import com.ideamanagement.dto.ApiEndpointDto;
import com.ideamanagement.entity.ApiEndpoint;
import com.ideamanagement.service.ApiEndpointService;
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
@RequestMapping("/api/endpoints")
@RequiredArgsConstructor
@Tag(name = "API Endpoint Management", description = "APIs for managing API endpoints")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, allowCredentials = "true")
public class ApiEndpointController {
    private final ApiEndpointService apiEndpointService;

    @PostMapping
    @Operation(summary = "Create a new API endpoint", description = "Creates a new API endpoint with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoint created successfully",
            content = @Content(schema = @Schema(implementation = ApiEndpointDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiEndpointDto> createEndpoint(
        @Parameter(description = "API endpoint details", required = true)
        @Valid @RequestBody ApiEndpointDto endpointDto) {
        return ResponseEntity.ok(apiEndpointService.createEndpoint(endpointDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an API endpoint", description = "Updates an existing API endpoint with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoint updated successfully",
            content = @Content(schema = @Schema(implementation = ApiEndpointDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "API endpoint not found")
    })
    public ResponseEntity<ApiEndpointDto> updateEndpoint(
        @Parameter(description = "ID of the API endpoint to update", required = true)
        @PathVariable UUID id,
        @Parameter(description = "Updated API endpoint details", required = true)
        @Valid @RequestBody ApiEndpointDto endpointDto) {
        return ResponseEntity.ok(apiEndpointService.updateEndpoint(id, endpointDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an API endpoint", description = "Deletes an API endpoint by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "API endpoint deleted successfully"),
        @ApiResponse(responseCode = "404", description = "API endpoint not found")
    })
    public ResponseEntity<Void> deleteEndpoint(
        @Parameter(description = "ID of the API endpoint to delete", required = true)
        @PathVariable UUID id) {
        apiEndpointService.deleteEndpoint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get API endpoint by ID", description = "Retrieves an API endpoint by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoint retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApiEndpointDto.class))),
        @ApiResponse(responseCode = "404", description = "API endpoint not found")
    })
    public ResponseEntity<ApiEndpointDto> getEndpointById(
        @Parameter(description = "ID of the API endpoint to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(apiEndpointService.getEndpointById(id));
    }

    @GetMapping
    @Operation(summary = "Get all API endpoints", description = "Retrieves a list of all API endpoints with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoints retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiEndpointDto>> getAllEndpoints(
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiEndpointService.getAllEndpoints(pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get API endpoints by status", description = "Retrieves API endpoints with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoints retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiEndpointDto>> getEndpointsByStatus(
        @Parameter(description = "Status of the API endpoints", required = true)
        @PathVariable ApiEndpoint.Status status,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiEndpointService.getEndpointsByStatus(status, pageable));
    }

    @GetMapping("/method/{method}")
    @Operation(summary = "Get API endpoints by method", description = "Retrieves API endpoints with a specific HTTP method")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoints retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiEndpointDto>> getEndpointsByMethod(
        @Parameter(description = "HTTP method of the API endpoints", required = true)
        @PathVariable ApiEndpoint.HttpMethod method,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiEndpointService.getEndpointsByMethod(method, pageable));
    }

    @GetMapping("/version/{version}")
    @Operation(summary = "Get API endpoints by version", description = "Retrieves API endpoints with a specific version")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API endpoints retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiEndpointDto>> getEndpointsByVersion(
        @Parameter(description = "Version of the API endpoints", required = true)
        @PathVariable String version,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiEndpointService.getEndpointsByVersion(version, pageable));
    }
} 