package com.ideamanagement.repository;

import com.ideamanagement.entity.IntegrationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IntegrationSettingsRepository extends JpaRepository<IntegrationSettings, UUID> {
    List<IntegrationSettings> findByUserId(UUID userId);
    Optional<IntegrationSettings> findByUserIdAndType(UUID userId, IntegrationSettings.IntegrationType type);
    boolean existsByUserIdAndType(UUID userId, IntegrationSettings.IntegrationType type);
} 