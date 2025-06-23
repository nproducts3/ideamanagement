package com.ideamanagement.repository;

import com.ideamanagement.entity.ApiTestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiTestLogRepository extends JpaRepository<ApiTestLog, UUID> {
    Page<ApiTestLog> findByEndpointId(UUID endpointId, Pageable pageable);
} 