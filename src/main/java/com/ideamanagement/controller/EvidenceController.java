package com.ideamanagement.controller;

import com.ideamanagement.dto.EvidenceDto;
import com.ideamanagement.entity.Evidence;
import com.ideamanagement.service.EvidenceService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/evidence")
@RequiredArgsConstructor
@Tag(name = "Evidence Management", description = "APIs for managing evidence items")
public class EvidenceController {
    private final EvidenceService evidenceService;

    @PostMapping("/upload")
    @Operation(summary = "Upload evidence file", description = "Uploads a file as evidence with basic metadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> uploadEvidence(
            @Parameter(description = "Evidence file", required = true) @RequestPart("file") MultipartFile file,
            @Parameter(description = "Project ID", required = true) @RequestParam("projectId") UUID projectId,
            @Parameter(description = "Uploader ID", required = true) @RequestParam("uploadedBy") UUID uploadedBy,
            @Parameter(description = "Title", required = true) @RequestParam("title") String title,
            @Parameter(description = "Category", required = true) @RequestParam("category") String category,
            @Parameter(description = "Description") @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Idea ID") @RequestParam(value = "ideaId", required = false) UUID ideaId,
            @Parameter(description = "Tags") @RequestParam(value = "tags", required = false) List<String> tags) {
        
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }

            EvidenceDto evidenceDto = new EvidenceDto();
            evidenceDto.setTitle(title);
            evidenceDto.setDescription(description);
            evidenceDto.setType(Evidence.EvidenceType.FILE);
            evidenceDto.setCategory(category);
            evidenceDto.setProjectId(projectId);
            evidenceDto.setUploadedBy(uploadedBy);
            evidenceDto.setIdeaId(ideaId);
            evidenceDto.setFileName(file.getOriginalFilename());
            evidenceDto.setFileSize(file.getSize());
            evidenceDto.setFileData(file);
            if (tags != null) {
                evidenceDto.setTags(new HashSet<>(tags));
            }

            return ResponseEntity.ok(evidenceService.createEvidence(evidenceDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading evidence: " + e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new evidence", description = "Creates a new evidence item with the provided details and optional file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> createEvidence(
            @Parameter(description = "Evidence title", required = true) @RequestParam(required = true) String title,
            @Parameter(description = "Evidence description") @RequestParam(required = false) String description,
            @Parameter(description = "Evidence type (FILE, LINK, TEXT)", required = true) @RequestParam(required = true) Evidence.EvidenceType type,
            @Parameter(description = "Evidence category", required = true) @RequestParam(required = true) String category,
            @Parameter(description = "ID of the project this evidence belongs to", required = true) @RequestParam(required = true) UUID projectId,
            @Parameter(description = "ID of the user who uploaded this evidence", required = true) @RequestParam(required = true) UUID uploadedBy,
            @Parameter(description = "ID of the idea this evidence is linked to (optional)") @RequestParam(required = false) UUID ideaId,
            @Parameter(description = "Evidence file (required if type is FILE)") @RequestPart(required = false) MultipartFile file,
            @Parameter(description = "Evidence URL (required if type is LINK)") @RequestParam(required = false) String url,
            @Parameter(description = "Evidence tags (comma-separated)") @RequestParam(required = false) List<String> tags) {

        try {
            // Validate required parameters
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required");
            }
            if (type == null) {
                return ResponseEntity.badRequest().body("Type is required");
            }
            if (category == null || category.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category is required");
            }
            if (projectId == null) {
                return ResponseEntity.badRequest().body("Project ID is required");
            }
            if (uploadedBy == null) {
                return ResponseEntity.badRequest().body("Uploader ID is required");
            }

            // Validate type-specific requirements
            if (type == Evidence.EvidenceType.FILE && file == null) {
                return ResponseEntity.badRequest().body("File is required for type FILE");
            }
            if (type == Evidence.EvidenceType.LINK && (url == null || url.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("URL is required for type LINK");
            }

            EvidenceDto evidenceDto = new EvidenceDto();
            evidenceDto.setTitle(title.trim());
            evidenceDto.setDescription(description != null ? description.trim() : null);
            evidenceDto.setType(type);
            evidenceDto.setCategory(category.trim());
            evidenceDto.setProjectId(projectId);
            evidenceDto.setUploadedBy(uploadedBy);
            evidenceDto.setIdeaId(ideaId);
            evidenceDto.setUrl(url != null ? url.trim() : null);
            if (tags != null) {
                evidenceDto.setTags(new HashSet<>(tags));
            }

            if (file != null) {
                evidenceDto.setFileName(file.getOriginalFilename());
                evidenceDto.setFileSize(file.getSize());
                evidenceDto.setFileData(file);
            }

            return ResponseEntity.ok(evidenceService.createEvidence(evidenceDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating evidence: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evidence by ID", description = "Retrieves an evidence item by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "404", description = "Evidence not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<EvidenceDto> getEvidence(
            @Parameter(description = "ID of the evidence to retrieve", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(evidenceService.getEvidence(id));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get evidence by project", description = "Retrieves a paginated list of evidence items for a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved evidence list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<EvidenceDto>> getEvidenceByProject(
            @Parameter(description = "ID of the project to retrieve evidence for", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(evidenceService.getEvidenceByProject(projectId, pageable));
    }

    @GetMapping("/project/{projectId}/category/{category}")
    @Operation(summary = "Get evidence by project and category", description = "Retrieves evidence items for a project filtered by category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved evidence list by category",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "404", description = "Project or category not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<EvidenceDto>> getEvidenceByProjectAndCategory(
            @Parameter(description = "ID of the project", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Category name to filter by", required = true) @PathVariable String category) {
        return ResponseEntity.ok(evidenceService.getEvidenceByProjectAndCategory(projectId, category));
    }

    @GetMapping("/project/{projectId}/tag/{tag}")
    @Operation(summary = "Get evidence by project and tag", description = "Retrieves evidence items for a project filtered by tag.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved evidence list by tag",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "404", description = "Project or tag not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<EvidenceDto>> getEvidenceByProjectAndTag(
            @Parameter(description = "ID of the project", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Tag name to filter by", required = true) @PathVariable String tag) {
        return ResponseEntity.ok(evidenceService.getEvidenceByProjectAndTag(projectId, tag));
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @Operation(summary = "Get evidence by project and status", description = "Retrieves evidence items for a project filtered by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved evidence list by status",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "404", description = "Project or status not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<EvidenceDto>> getEvidenceByProjectAndStatus(
            @Parameter(description = "ID of the project", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Evidence status to filter by", required = true) @PathVariable Evidence.EvidenceStatus status) {
        return ResponseEntity.ok(evidenceService.getEvidenceByProjectAndStatus(projectId, status));
    }

    @GetMapping
    @Operation(summary = "Get all evidence items", description = "Retrieves a paginated list of all evidence items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved evidence list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<EvidenceDto>> getAllEvidence(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(evidenceService.getAllEvidence(pageable));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update evidence", description = "Updates an existing evidence item with new details and optional file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json")), // Adjust schema for error response
            @ApiResponse(responseCode = "404", description = "Evidence not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<EvidenceDto> updateEvidence(
            @Parameter(description = "ID of the evidence to update", required = true) @PathVariable UUID id,
            @Parameter(description = "Evidence title") @RequestParam(required = false) String title,
            @Parameter(description = "Evidence description") @RequestParam(required = false) String description,
            @Parameter(description = "Evidence type (FILE, LINK, TEXT)") @RequestParam(required = false) Evidence.EvidenceType type,
            @Parameter(description = "Evidence category") @RequestParam(required = false) String category,
            @Parameter(description = "Evidence file") @RequestPart(required = false) MultipartFile file,
            @Parameter(description = "Evidence URL") @RequestParam(required = false) String url,
            @Parameter(description = "Evidence tags (comma-separated)") @RequestParam(required = false) List<String> tags) {

        EvidenceDto evidenceDto = new EvidenceDto();
        // Only set fields that are provided in the request to allow partial updates
        evidenceDto.setTitle(title);
        evidenceDto.setDescription(description);
        evidenceDto.setType(type);
        evidenceDto.setCategory(category);
        evidenceDto.setUrl(url);
        if (tags != null) {
            evidenceDto.setTags(new HashSet<>(tags));
        }
        if (file != null) {
            evidenceDto.setFileName(file.getOriginalFilename());
            evidenceDto.setFileSize(file.getSize());
            evidenceDto.setFileData(file);
        }

        return ResponseEntity.ok(evidenceService.updateEvidence(id, evidenceDto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update evidence status", description = "Updates the status of an evidence item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvidenceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status provided",
                    content = @Content(mediaType = "application/json")), // Adjust schema for error response
            @ApiResponse(responseCode = "404", description = "Evidence not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<EvidenceDto> updateEvidenceStatus(
            @Parameter(description = "ID of the evidence to update status for", required = true) @PathVariable UUID id,
            @Parameter(description = "New status for the evidence (PENDING, APPROVED, REJECTED, ARCHIVED)", required = true) @RequestBody Evidence.EvidenceStatus status) {
        return ResponseEntity.ok(evidenceService.updateEvidenceStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evidence", description = "Deletes an evidence item by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence deleted successfully",
                    content = @Content(mediaType = "application/json")), // No content for successful delete
            @ApiResponse(responseCode = "404", description = "Evidence not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteEvidence(
            @Parameter(description = "ID of the evidence to delete", required = true) @PathVariable UUID id) {
        evidenceService.deleteEvidence(id);
        return ResponseEntity.noContent().build(); // Use noContent for successful deletion with no body
    }
} 