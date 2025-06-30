package com.ideamanagement.repository;

import com.ideamanagement.entity.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, UUID> {
    Page<Deployment> findByEnvironment(String environment, Pageable pageable);
    Page<Deployment> findByStatus(Deployment.DeploymentStatus status, Pageable pageable);
    Page<Deployment> findByHealth(Deployment.HealthStatus health, Pageable pageable);
    Page<Deployment> findByVersion(String version, Pageable pageable);
    Page<Deployment> findByEmployeeId(UUID employeeId, Pageable pageable);
    Deployment findByIdAndEmployeeId(UUID id, UUID employeeId);
    void deleteByIdAndEmployeeId(UUID id, UUID employeeId);
} 