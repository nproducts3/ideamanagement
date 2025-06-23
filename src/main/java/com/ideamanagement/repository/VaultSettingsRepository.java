package com.ideamanagement.repository;

import com.ideamanagement.entity.VaultSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VaultSettingsRepository extends JpaRepository<VaultSettings, UUID> {
    // Since this is a single-row configuration table, we can add a method to get the first record
    VaultSettings findFirstByOrderByIdAsc();
} 