package com.ideamanagement.controller;

import com.ideamanagement.dto.TagDto;
import com.ideamanagement.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tag", description = "Tag management APIs")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @Operation(
        summary = "Create a new tag",
        description = "Creates a new tag with the provided name"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tag created successfully",
            content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or tag name already exists")
    })
    public ResponseEntity<TagDto> createTag(
        @Parameter(description = "Tag to create", required = true)
        @RequestBody TagDto tagDto) {
        return ResponseEntity.ok(tagService.createTag(tagDto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing tag",
        description = "Updates the tag with the specified ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tag updated successfully",
            content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input or tag name already exists")
    })
    public ResponseEntity<TagDto> updateTag(
        @Parameter(description = "ID of the tag to update", required = true)
        @PathVariable String id,
        @Parameter(description = "Updated tag information", required = true)
        @RequestBody TagDto tagDto) {
        return ResponseEntity.ok(tagService.updateTag(id, tagDto));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a tag by ID",
        description = "Retrieves a tag by their ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tag found",
            content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<TagDto> getTag(
        @Parameter(description = "ID of the tag to retrieve", required = true)
        @PathVariable String id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping
    @Operation(
        summary = "Get all tags",
        description = "Retrieves a list of all tags in the system"
    )
    @ApiResponse(responseCode = "200", description = "List of tags retrieved successfully",
        content = @Content(schema = @Schema(implementation = TagDto.class)))
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a tag",
        description = "Deletes the tag with the specified ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Void> deleteTag(
        @Parameter(description = "ID of the tag to delete", required = true)
        @PathVariable String id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
} 