package com.ideamanagement.repository;

import com.ideamanagement.entity.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, UUID> {
    Optional<Environment> findByName(String name);
    Page<Environment> findByStatus(Environment.EnvironmentStatus status, Pageable pageable);
} 