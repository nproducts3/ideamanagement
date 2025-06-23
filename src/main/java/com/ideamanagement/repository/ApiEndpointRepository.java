package com.ideamanagement.repository;

import com.ideamanagement.entity.ApiEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, UUID> {
    Page<ApiEndpoint> findByStatus(ApiEndpoint.Status status, Pageable pageable);
    Page<ApiEndpoint> findByMethod(ApiEndpoint.HttpMethod method, Pageable pageable);
    Page<ApiEndpoint> findByVersion(String version, Pageable pageable);
} 