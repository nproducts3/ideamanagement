package com.ideamanagement.controller;

import com.ideamanagement.dto.ApiTestLogDto;
import com.ideamanagement.service.ApiTestLogService;
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
@RequestMapping("/api/test-logs")
@RequiredArgsConstructor
@Tag(name = "API Test Log Management", description = "APIs for managing API test logs")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, allowCredentials = "true")
public class ApiTestLogController {
    private final ApiTestLogService apiTestLogService;

    @PostMapping
    @Operation(summary = "Create a new API test log", description = "Creates a new API test log with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API test log created successfully",
            content = @Content(schema = @Schema(implementation = ApiTestLogDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "API endpoint not found")
    })
    public ResponseEntity<ApiTestLogDto> createTestLog(
        @Parameter(description = "API test log details", required = true)
        @Valid @RequestBody ApiTestLogDto testLogDto) {
        return ResponseEntity.ok(apiTestLogService.createTestLog(testLogDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get API test log by ID", description = "Retrieves an API test log by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API test log retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApiTestLogDto.class))),
        @ApiResponse(responseCode = "404", description = "API test log not found")
    })
    public ResponseEntity<ApiTestLogDto> getTestLogById(
        @Parameter(description = "ID of the API test log to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(apiTestLogService.getTestLogById(id));
    }

    @GetMapping("/endpoint/{endpointId}")
    @Operation(summary = "Get API test logs by endpoint ID", description = "Retrieves API test logs for a specific endpoint")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API test logs retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiTestLogDto>> getTestLogsByEndpointId(
        @Parameter(description = "ID of the API endpoint", required = true)
        @PathVariable UUID endpointId,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiTestLogService.getTestLogsByEndpointId(endpointId, pageable));
    }

    @GetMapping
    @Operation(summary = "Get all API test logs", description = "Retrieves a list of all API test logs with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API test logs retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<ApiTestLogDto>> getAllTestLogs(
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(apiTestLogService.getAllTestLogs(pageable));
    }
} 