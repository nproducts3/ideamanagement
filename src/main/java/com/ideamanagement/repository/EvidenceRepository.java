package com.ideamanagement.repository;

import com.ideamanagement.entity.Evidence;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.entity.Project;
import com.ideamanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {
    Page<Evidence> findByProjectId(UUID projectId, Pageable pageable);
    
    List<Evidence> findByProjectIdAndCategory(UUID projectId, String category);
    
    @Query("SELECT e FROM Evidence e WHERE e.project.id = ?1 AND ?2 MEMBER OF e.tags")
    List<Evidence> findByProjectIdAndTag(UUID projectId, String tag);
    
    List<Evidence> findByProjectIdAndStatus(UUID projectId, Evidence.EvidenceStatus status);
    
    Page<Evidence> findByUploadedBy(User uploadedBy, Pageable pageable);
    
    Page<Evidence> findByIdea(Idea idea, Pageable pageable);

    List<Evidence> findByProjectIdAndTagsContaining(UUID projectId, String tag);
} 