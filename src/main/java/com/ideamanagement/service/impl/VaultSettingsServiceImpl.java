package com.ideamanagement.service.impl;

import com.ideamanagement.dto.VaultSettingsDto;
import com.ideamanagement.entity.VaultSettings;
import com.ideamanagement.repository.VaultSettingsRepository;
import com.ideamanagement.service.VaultSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VaultSettingsServiceImpl implements VaultSettingsService {
    private final VaultSettingsRepository vaultSettingsRepository;

    @Override
    @Transactional(readOnly = true)
    public VaultSettingsDto getVaultSettings() {
        VaultSettings settings = vaultSettingsRepository.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new VaultSettings();
            settings = vaultSettingsRepository.save(settings);
        }
        return convertToDto(settings);
    }

    @Override
    public VaultSettingsDto updateVaultSettings(VaultSettingsDto vaultSettingsDto) {
        VaultSettings settings = vaultSettingsRepository.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new VaultSettings();
        }

        settings.setEncryption(vaultSettingsDto.getEncryption());
        settings.setBackup(vaultSettingsDto.getBackup());
        settings = vaultSettingsRepository.save(settings);

        return convertToDto(settings);
    }

    @Override
    public VaultSettingsDto updateEncryptionStatus(VaultSettings.EncryptionStatus status) {
        VaultSettings settings = vaultSettingsRepository.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new VaultSettings();
        }

        settings.setEncryption(status);
        settings = vaultSettingsRepository.save(settings);

        return convertToDto(settings);
    }

    @Override
    public VaultSettingsDto updateBackupStatus(VaultSettings.BackupStatus status) {
        VaultSettings settings = vaultSettingsRepository.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new VaultSettings();
        }

        settings.setBackup(status);
        settings = vaultSettingsRepository.save(settings);

        return convertToDto(settings);
    }

    private VaultSettingsDto convertToDto(VaultSettings settings) {
        VaultSettingsDto dto = new VaultSettingsDto();
        dto.setId(settings.getId());
        dto.setEncryption(settings.getEncryption());
        dto.setBackup(settings.getBackup());
        dto.setUpdatedAt(settings.getUpdatedAt());
        return dto;
    }
} 