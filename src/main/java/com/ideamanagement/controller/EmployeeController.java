package com.ideamanagement.controller;

import com.ideamanagement.dto.EmployeeDto;
import com.ideamanagement.entity.Employee;
import com.ideamanagement.service.EmployeeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Creates a new employee with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee created successfully",
            content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<EmployeeDto> createEmployee(
        @Parameter(description = "Employee details", required = true)
        @Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee", description = "Updates an existing employee by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee updated successfully",
            content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDto> updateEmployee(
        @Parameter(description = "ID of the employee to update", required = true)
        @PathVariable UUID id,
        @Parameter(description = "Updated employee details", required = true)
        @Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee", description = "Deletes an employee by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "ID of the employee to delete", required = true)
        @PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee retrieved successfully",
            content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDto> getEmployeeById(
        @Parameter(description = "ID of the employee to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get employee by email", description = "Retrieves an employee by their email address")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee retrieved successfully",
            content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(
        @Parameter(description = "Email to search for", required = true)
        @PathVariable String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves a paginated list of all employees")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(
        @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get employees by status", description = "Retrieves employees with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<EmployeeDto>> getEmployeesByStatus(
        @Parameter(description = "Employee status", required = true)
        @PathVariable Employee.Status status,
        @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getEmployeesByStatus(status, pageable));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get employees by department", description = "Retrieves employees in a specific department")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<EmployeeDto>> getEmployeesByDepartment(
        @Parameter(description = "Department name", required = true)
        @PathVariable String department,
        @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department, pageable));
    }

    @GetMapping("/check/email/{email}")
    @Operation(summary = "Check email availability", description = "Checks if an email is already registered")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email availability checked successfully")
    })
    public ResponseEntity<Boolean> checkEmailExists(
        @Parameter(description = "Email to check", required = true)
        @PathVariable String email) {
        return ResponseEntity.ok(employeeService.existsByEmail(email));
    }
} 