package com.ideamanagement.controller;

import com.ideamanagement.dto.UserDto;
import com.ideamanagement.entity.User;
import com.ideamanagement.exception.DuplicateResourceException;
import com.ideamanagement.service.UserService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided details. Username must be 3-50 characters and can contain letters, numbers, dots, underscores, and hyphens. Email must be valid. Password must be at least 8 characters."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate username/email",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> createUser(
        @Parameter(description = "User details", required = true)
        @Valid @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.createUser(userDto));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating user: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their UUID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserById(
        @Parameter(description = "UUID of the user to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    @Operation(
        summary = "Get user by username",
        description = "Retrieves a user by their username"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserByUsername(
        @Parameter(description = "Username to search for", required = true)
        @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    @Operation(
        summary = "Get user by email",
        description = "Retrieves a user by their email address"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserByEmail(
        @Parameter(description = "Email to search for", required = true)
        @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieves a paginated list of all users. Supports sorting and pagination."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<UserDto>> getAllUsers(
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a user",
        description = "Updates an existing user with the provided details. All fields are optional except ID. Password is only updated if provided."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<UserDto> updateUser(
        @Parameter(description = "UUID of the user to update", required = true)
        @PathVariable UUID id,
        @Parameter(description = "Updated user details", required = true)
        @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by their UUID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "UUID of the user to delete", required = true)
        @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/username/{username}")
    @Operation(
        summary = "Check username availability",
        description = "Checks if a username is already taken"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Username availability checked successfully")
    })
    public ResponseEntity<Boolean> checkUsernameExists(
        @Parameter(description = "Username to check", required = true)
        @PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/check/email/{email}")
    @Operation(
        summary = "Check email availability",
        description = "Checks if an email is already registered"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email availability checked successfully")
    })
    public ResponseEntity<Boolean> checkEmailExists(
        @Parameter(description = "Email to check", required = true)
        @PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
} 