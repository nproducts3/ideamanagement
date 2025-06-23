package com.ideamanagement.service.impl;

import com.ideamanagement.dto.EvidenceDto;
import com.ideamanagement.entity.Evidence;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.entity.Project;
import com.ideamanagement.entity.User;
import com.ideamanagement.exception.DuplicateResourceException;
import com.ideamanagement.exception.ResourceNotFoundException;
import com.ideamanagement.repository.EvidenceRepository;
import com.ideamanagement.repository.IdeaRepository;
import com.ideamanagement.repository.ProjectRepository;
import com.ideamanagement.repository.UserRepository;
import com.ideamanagement.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvidenceServiceImpl implements EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    // Define a directory for storing uploaded files
    // You might want to make this configurable in application.properties
    private final String uploadDir = "uploads/evidence";

    @Override
    public EvidenceDto createEvidence(EvidenceDto evidenceDto) {
        // Basic validation for required fields from DTO
        if (!StringUtils.hasText(evidenceDto.getTitle())) {
            throw new IllegalArgumentException("Evidence title is required.");
        }
        if (evidenceDto.getType() == null) {
            throw new IllegalArgumentException("Evidence type is required.");
        }
        if (!StringUtils.hasText(evidenceDto.getCategory())) {
            throw new IllegalArgumentException("Evidence category is required.");
        }
        if (evidenceDto.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID is required.");
        }
        if (evidenceDto.getUploadedBy() == null) {
            throw new IllegalArgumentException("Uploader ID is required.");
        }

        Project project = projectRepository.findById(evidenceDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + evidenceDto.getProjectId()));
        User uploadedBy = userRepository.findById(evidenceDto.getUploadedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + evidenceDto.getUploadedBy()));
        Idea idea = null;
        if (evidenceDto.getIdeaId() != null) {
            idea = ideaRepository.findById(evidenceDto.getIdeaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + evidenceDto.getIdeaId()));
        }

        Evidence evidence = new Evidence();
        evidence.setTitle(evidenceDto.getTitle());
        evidence.setDescription(evidenceDto.getDescription());
        evidence.setType(evidenceDto.getType());
        evidence.setCategory(evidenceDto.getCategory());
        evidence.setStatus(evidenceDto.getStatus() != null ? evidenceDto.getStatus() : Evidence.EvidenceStatus.PENDING);
        evidence.setUrl(evidenceDto.getUrl());
        evidence.setProject(project);
        evidence.setUploadedBy(uploadedBy);
        evidence.setIdea(idea);
        evidence.setTags(evidenceDto.getTags());

        // Handle file upload if type is FILE and a file is provided
        if (evidenceDto.getType() == Evidence.EvidenceType.FILE && evidenceDto.getFileName() != null) {
            if (evidenceDto.getFileData() == null) {
                throw new IllegalArgumentException("File data is missing for evidence type FILE.");
            }
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(uploadPath);
                String uniqueFileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(evidenceDto.getFileName());
                Path targetLocation = uploadPath.resolve(uniqueFileName);
                Files.copy(evidenceDto.getFileData().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                evidence.setFileName(uniqueFileName);
                evidence.setFilePath(targetLocation.toString());
                evidence.setContentType(evidenceDto.getFileData().getContentType());
                evidence.setFileSize(evidenceDto.getFileData().getSize());
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + evidenceDto.getFileName() + ". Please try again!", ex);
            }
        }

        Evidence savedEvidence = evidenceRepository.save(evidence);
        return convertToDto(savedEvidence);
    }

    @Override
    @Transactional(readOnly = true)
    public EvidenceDto getEvidence(UUID id) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + id));
        return convertToDto(evidence);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvidenceDto> getAllEvidence(Pageable pageable) {
        return evidenceRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvidenceDto> getEvidenceByProject(UUID projectId, Pageable pageable) {
        return evidenceRepository.findByProjectId(projectId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvidenceDto> getEvidenceByProjectAndCategory(UUID projectId, String category) {
        return evidenceRepository.findByProjectIdAndCategory(projectId, category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvidenceDto> getEvidenceByProjectAndTag(UUID projectId, String tag) {
        return evidenceRepository.findByProjectIdAndTagsContaining(projectId, tag).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvidenceDto> getEvidenceByProjectAndStatus(UUID projectId, Evidence.EvidenceStatus status) {
        return evidenceRepository.findByProjectIdAndStatus(projectId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvidenceDto updateEvidence(UUID id, EvidenceDto evidenceDto) {
        Evidence existingEvidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + id));

        // Update fields
        existingEvidence.setTitle(evidenceDto.getTitle());
        existingEvidence.setDescription(evidenceDto.getDescription());
        existingEvidence.setType(evidenceDto.getType());
        existingEvidence.setCategory(evidenceDto.getCategory());
        existingEvidence.setStatus(evidenceDto.getStatus());
        existingEvidence.setUrl(evidenceDto.getUrl());
        existingEvidence.setTags(evidenceDto.getTags());

        // File-related updates (if a file is part of the update or URL changes)
        if (evidenceDto.getType() == Evidence.EvidenceType.FILE && evidenceDto.getFileName() != null) {
            // Simplified: for a real app, you'd handle file replacement/deletion
            // For now, if a new file comes, it's just set. Old file might remain.
            if (evidenceDto.getFileData() != null) {
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                try {
                    Files.createDirectories(uploadPath);
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(evidenceDto.getFileName());
                    Path targetLocation = uploadPath.resolve(uniqueFileName);
                    Files.copy(evidenceDto.getFileData().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    existingEvidence.setFileName(uniqueFileName);
                    existingEvidence.setFilePath(targetLocation.toString());
                    existingEvidence.setContentType(evidenceDto.getFileData().getContentType());
                    existingEvidence.setFileSize(evidenceDto.getFileData().getSize());
                } catch (IOException ex) {
                    throw new RuntimeException("Could not update file " + evidenceDto.getFileName() + ". Please try again!", ex);
                }
            }
        } else if (existingEvidence.getType() == Evidence.EvidenceType.FILE && evidenceDto.getType() != Evidence.EvidenceType.FILE) {
            // If changing from FILE to another type, clear file-related data (simplified)
            existingEvidence.setFileName(null);
            existingEvidence.setFilePath(null);
            existingEvidence.setContentType(null);
            existingEvidence.setFileSize(null);
        }

        Evidence updatedEvidence = evidenceRepository.save(existingEvidence);
        return convertToDto(updatedEvidence);
    }

    @Override
    public EvidenceDto updateEvidenceStatus(UUID id, Evidence.EvidenceStatus status) {
        Evidence existingEvidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + id));
        existingEvidence.setStatus(status);
        Evidence updatedEvidence = evidenceRepository.save(existingEvidence);
        return convertToDto(updatedEvidence);
    }

    @Override
    public void deleteEvidence(UUID id) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + id));
        // Optionally delete the file from the file system as well
        if (evidence.getFilePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(evidence.getFilePath()));
            } catch (IOException e) {
                // Log error, but don't prevent deletion of the database record
                System.err.println("Could not delete file from file system: " + evidence.getFilePath());
            }
        }
        evidenceRepository.deleteById(id);
    }

    private EvidenceDto convertToDto(Evidence evidence) {
        EvidenceDto dto = new EvidenceDto();
        dto.setId(evidence.getId());
        dto.setTitle(evidence.getTitle());
        dto.setDescription(evidence.getDescription());
        dto.setType(evidence.getType());
        dto.setCategory(evidence.getCategory());
        dto.setStatus(evidence.getStatus());
        dto.setFileName(evidence.getFileName());
        dto.setFilePath(evidence.getFilePath());
        dto.setContentType(evidence.getContentType());
        dto.setFileSize(evidence.getFileSize());
        dto.setUrl(evidence.getUrl());
        dto.setUploadedAt(evidence.getUploadedAt());
        dto.setUpdatedAt(evidence.getUpdatedAt());
        dto.setTags(evidence.getTags());
        if (evidence.getIdea() != null) {
            dto.setIdeaId(evidence.getIdea().getId());
        }
        if (evidence.getProject() != null) {
            dto.setProjectId(evidence.getProject().getId());
        }
        if (evidence.getUploadedBy() != null) {
            dto.setUploadedBy(evidence.getUploadedBy().getId());
        }
        return dto;
    }

    // Helper method to convert DTO to Entity for creation/update
    private Evidence convertToEntity(EvidenceDto dto) {
        Evidence evidence = new Evidence();
        evidence.setId(dto.getId()); // ID might be null for new creation
        evidence.setTitle(dto.getTitle());
        evidence.setDescription(dto.getDescription());
        evidence.setType(dto.getType());
        evidence.setCategory(dto.getCategory());
        evidence.setStatus(dto.getStatus());
        evidence.setFileName(dto.getFileName());
        evidence.setFilePath(dto.getFilePath());
        evidence.setContentType(dto.getContentType());
        evidence.setFileSize(dto.getFileSize());
        evidence.setUrl(dto.getUrl());
        evidence.setTags(dto.getTags());

        // Relationships - fetch entities
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));
            evidence.setProject(project);
        }
        if (dto.getUploadedBy() != null) {
            User uploadedBy = userRepository.findById(dto.getUploadedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUploadedBy()));
            evidence.setUploadedBy(uploadedBy);
        }
        if (dto.getIdeaId() != null) {
            Idea idea = ideaRepository.findById(dto.getIdeaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + dto.getIdeaId()));
            evidence.setIdea(idea);
        }
        return evidence;
    }
} 