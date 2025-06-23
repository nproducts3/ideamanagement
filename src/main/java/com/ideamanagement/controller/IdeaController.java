package com.ideamanagement.controller;

import com.ideamanagement.dto.IdeaDto;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.service.IdeaService;
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
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
@Tag(name = "Idea Management", description = "APIs for managing ideas")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, allowCredentials = "true")
public class IdeaController {

    private final IdeaService ideaService;

    @PostMapping
    @Operation(
        summary = "Create a new idea",
        description = "Creates a new idea with the provided details"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Idea created successfully",
            content = @Content(schema = @Schema(implementation = IdeaDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<IdeaDto> createIdea(
        @Parameter(description = "Idea details", required = true)
        @Valid @RequestBody IdeaDto ideaDto) {
        return ResponseEntity.ok(ideaService.createIdea(ideaDto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an idea",
        description = "Updates an existing idea with the provided details"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Idea updated successfully",
            content = @Content(schema = @Schema(implementation = IdeaDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Idea not found")
    })
    public ResponseEntity<IdeaDto> updateIdea(
        @Parameter(description = "ID of the idea to update", required = true)
        @PathVariable UUID id,
        @Parameter(description = "Updated idea details", required = true)
        @Valid @RequestBody IdeaDto ideaDto) {
        return ResponseEntity.ok(ideaService.updateIdea(id, ideaDto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an idea",
        description = "Deletes an idea by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Idea deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Idea not found")
    })
    public ResponseEntity<Void> deleteIdea(
        @Parameter(description = "ID of the idea to delete", required = true)
        @PathVariable UUID id) {
        ideaService.deleteIdea(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get idea by ID",
        description = "Retrieves an idea by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Idea retrieved successfully",
            content = @Content(schema = @Schema(implementation = IdeaDto.class))),
        @ApiResponse(responseCode = "404", description = "Idea not found")
    })
    public ResponseEntity<IdeaDto> getIdeaById(
        @Parameter(description = "ID of the idea to retrieve", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(ideaService.getIdeaById(id));
    }

    @GetMapping
    @Operation(
        summary = "Get all ideas",
        description = "Retrieves a list of all ideas with pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ideas retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<IdeaDto>> getAllIdeas(
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(ideaService.getAllIdeas(pageable));
    }

    @GetMapping("/assigned/{assignee}")
    @Operation(
        summary = "Get ideas by assignee",
        description = "Retrieves ideas assigned to a specific user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ideas retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<IdeaDto>> getIdeasByAssignee(
        @Parameter(description = "Name of the assignee", required = true)
        @PathVariable String assignee,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(ideaService.getIdeasByAssignee(assignee, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get ideas by status",
        description = "Retrieves ideas with a specific status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ideas retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<IdeaDto>> getIdeasByStatus(
        @Parameter(description = "Status of the ideas (PENDING, IN_PROGRESS, COMPLETED)", required = true)
        @PathVariable Idea.Status status,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(ideaService.getIdeasByStatus(status, pageable));
    }

    @GetMapping("/tag/{tag}")
    @Operation(
        summary = "Get ideas by tag",
        description = "Retrieves ideas containing a specific tag"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ideas retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<IdeaDto>> getIdeasByTag(
        @Parameter(description = "Tag to search for", required = true)
        @PathVariable String tag,
        @Parameter(description = "Pagination and sorting parameters")
        Pageable pageable) {
        return ResponseEntity.ok(ideaService.getIdeasByTag(tag, pageable));
    }
} 