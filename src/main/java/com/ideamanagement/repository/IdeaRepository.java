package com.ideamanagement.repository;

import com.ideamanagement.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, UUID> {
    Page<Idea> findByAssignedTo(String assignedTo, Pageable pageable);
    Page<Idea> findByStatus(Idea.Status status, Pageable pageable);
    Page<Idea> findByTagsContaining(String tag, Pageable pageable);
    Page<Idea> findByEmployeeId(UUID employeeId, Pageable pageable);
    Idea findByIdAndEmployeeId(UUID id, UUID employeeId);
    void deleteByIdAndEmployeeId(UUID id, UUID employeeId);
} 