package com.ideamanagement.service;

import com.ideamanagement.dto.EvidenceDto;
import com.ideamanagement.entity.Evidence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EvidenceService {
    EvidenceDto createEvidence(EvidenceDto evidenceDto);
    EvidenceDto getEvidence(UUID id);
    Page<EvidenceDto> getAllEvidence(Pageable pageable);
    Page<EvidenceDto> getEvidenceByProject(UUID projectId, Pageable pageable);
    List<EvidenceDto> getEvidenceByProjectAndCategory(UUID projectId, String category);
    List<EvidenceDto> getEvidenceByProjectAndTag(UUID projectId, String tag);
    List<EvidenceDto> getEvidenceByProjectAndStatus(UUID projectId, Evidence.EvidenceStatus status);
    EvidenceDto updateEvidence(UUID id, EvidenceDto evidenceDto);
    EvidenceDto updateEvidenceStatus(UUID id, Evidence.EvidenceStatus status);
    void deleteEvidence(UUID id);
    Page<EvidenceDto> getEvidenceByEmployee(UUID employeeId, Pageable pageable);
} 