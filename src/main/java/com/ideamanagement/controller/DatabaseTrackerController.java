package com.ideamanagement.controller;

import com.ideamanagement.dto.DatabaseTrackerDto;
import com.ideamanagement.entity.DatabaseTracker;
import com.ideamanagement.entity.DatabaseTracker.Status;
import com.ideamanagement.service.DatabaseTrackerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/database-trackers")
@RequiredArgsConstructor
@Tag(name = "Database Tracker", description = "Database tracker management APIs")
// @CrossOrigin(origins = "*", allowCredentials = "true")
public class DatabaseTrackerController {
    private final DatabaseTrackerService databaseTrackerService;

    @PostMapping
    @Operation(
        summary = "Create a new database tracker",
        description = "Creates a new database tracker with the provided information"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database tracker created successfully",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or name already exists")
    })
    public ResponseEntity<DatabaseTrackerDto> createDatabaseTracker(
        @RequestParam java.util.UUID employeeId,
        @RequestBody DatabaseTrackerDto dto) {
        dto.setEmployeeId(employeeId);
        return ResponseEntity.ok(databaseTrackerService.createDatabaseTracker(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing database tracker",
        description = "Updates the database tracker with the specified ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database tracker updated successfully",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "404", description = "Database tracker not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<DatabaseTrackerDto> updateDatabaseTracker(
        @Parameter(description = "ID of the database tracker to update", required = true)
        @PathVariable Integer id,
        @Parameter(description = "Updated database tracker information", required = true)
        @RequestParam java.util.UUID employeeId,
        @RequestBody DatabaseTrackerDto dto) {
        return ResponseEntity.ok(databaseTrackerService.updateDatabaseTracker(id, employeeId, dto));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a database tracker by ID",
        description = "Retrieves a database tracker by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database tracker found",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "404", description = "Database tracker not found")
    })
    public ResponseEntity<DatabaseTrackerDto> getDatabaseTracker(
        @Parameter(description = "ID of the database tracker to retrieve", required = true)
        @PathVariable Integer id,
        @Parameter(description = "Employee ID", required = true)
        @RequestParam java.util.UUID employeeId) {
        return ResponseEntity.ok(databaseTrackerService.getDatabaseTrackerById(id, employeeId));
    }

    @GetMapping
    @Operation(summary = "Get all database trackers with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved database trackers"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
    })
    public ResponseEntity<Page<DatabaseTrackerDto>> getAllDatabaseTrackers(
        @Parameter(description = "Employee ID", required = true)
        @RequestParam java.util.UUID employeeId,
        @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Sort field (valid values: id, name, version, status, lastModified, tablesCount, migrationsCount)", example = "lastModified") 
        @RequestParam(defaultValue = "lastModified") String sort,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "desc") String direction) {
        
        // Validate sort field
        List<String> validSortFields = Arrays.asList("id", "name", "version", "status", "lastModified", "tablesCount", "migrationsCount");
        if (!validSortFields.contains(sort)) {
            throw new IllegalArgumentException("Invalid sort field. Valid values are: " + String.join(", ", validSortFields));
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        return ResponseEntity.ok(databaseTrackerService.getAllDatabaseTrackers(employeeId, pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a database tracker",
        description = "Deletes the database tracker with the specified ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Database tracker deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Database tracker not found")
    })
    public ResponseEntity<Void> deleteDatabaseTracker(
        @Parameter(description = "ID of the database tracker to delete", required = true)
        @PathVariable Integer id,
        @Parameter(description = "Employee ID", required = true)
        @RequestParam java.util.UUID employeeId) {
        databaseTrackerService.deleteDatabaseTracker(id, employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get a database tracker by name",
        description = "Retrieves a database tracker by its name"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database tracker found",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "404", description = "Database tracker not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<DatabaseTrackerDto> getDatabaseTrackerByName(
        @Parameter(description = "Name of the database tracker to retrieve", required = true)
        @PathVariable String name) {
        return ResponseEntity.ok(databaseTrackerService.getDatabaseTrackerByName(name));
    }

    @Operation(
        summary = "Get database trackers by status",
        description = "Retrieves a paginated list of database trackers with the specified status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database trackers found",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<DatabaseTrackerDto>> getDatabaseTrackersByStatus(
        @Parameter(description = "Status to filter by", required = true)
        @PathVariable Status status,
        @Parameter(description = "Pagination information")
        Pageable pageable) {
        return ResponseEntity.ok(databaseTrackerService.getDatabaseTrackersByStatus(status, pageable));
    }

    @Operation(
        summary = "Get database trackers by version",
        description = "Retrieves a paginated list of database trackers with the specified version"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database trackers found",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class)))
    })
    @GetMapping("/version/{version}")
    public ResponseEntity<Page<DatabaseTrackerDto>> getDatabaseTrackersByVersion(
        @Parameter(description = "Version to filter by", required = true)
        @PathVariable String version,
        @Parameter(description = "Pagination information")
        Pageable pageable) {
        return ResponseEntity.ok(databaseTrackerService.getDatabaseTrackersByVersion(version, pageable));
    }

    @Operation(
        summary = "Update database tracker status",
        description = "Updates the status of the database tracker with the specified ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = DatabaseTrackerDto.class))),
        @ApiResponse(responseCode = "404", description = "Database tracker not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<DatabaseTrackerDto> updateStatus(
        @Parameter(description = "ID of the database tracker", required = true)
        @PathVariable Integer id,
        @Parameter(description = "New status", required = true)
        @RequestParam Status status) {
        return ResponseEntity.ok(databaseTrackerService.updateStatus(id, status));
    }
} 