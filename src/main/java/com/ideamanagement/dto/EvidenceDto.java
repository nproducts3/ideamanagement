package com.ideamanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ideamanagement.entity.Evidence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Evidence data transfer object")
public class EvidenceDto {
    @Schema(description = "Unique identifier of the evidence", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Title of the evidence", example = "Project Documentation")
    private String title;

    @Schema(description = "Detailed description of the evidence")
    private String description;

    @Schema(description = "Type of evidence (FILE, IMAGE, LINK)", example = "FILE")
    private Evidence.EvidenceType type;

    @Schema(description = "Category of the evidence", example = "development")
    private String category;

    @Schema(description = "Status of the evidence (PENDING, VALIDATED, REJECTED)", example = "PENDING")
    private Evidence.EvidenceStatus status;

    @Schema(description = "Name of the file if type is FILE", example = "document.pdf")
    private String fileName;

    @Schema(description = "Path to the file if type is FILE", example = "/path/to/document.pdf")
    private String filePath;

    @Schema(description = "Content type of the file if type is FILE", example = "application/pdf")
    private String contentType;

    @Schema(description = "Size of the file if type is FILE", example = "2.4 MB")
    private Long fileSize;

    @Schema(description = "URL if type is LINK", example = "https://example.com")
    private String url;

    @Schema(description = "Project ID this evidence belongs to")
    private UUID projectId;

    @Schema(description = "User ID who uploaded the evidence")
    private UUID uploadedBy;

    @Schema(description = "Upload timestamp")
    private LocalDateTime uploadedAt;

    @Schema(description = "Set of tags associated with the evidence")
    private Set<String> tags;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Idea ID this evidence belongs to")
    private UUID ideaId;

    @Schema(description = "File data for file uploads")
    private MultipartFile fileData;

    @Schema(description = "Employee ID associated with the evidence")
    private UUID employeeId;
} 