package com.ideamanagement.service;

import com.ideamanagement.dto.VaultSettingsDto;
import com.ideamanagement.entity.VaultSettings;

public interface VaultSettingsService {
    VaultSettingsDto getVaultSettings();
    VaultSettingsDto updateVaultSettings(VaultSettingsDto vaultSettingsDto);
    VaultSettingsDto updateEncryptionStatus(VaultSettings.EncryptionStatus status);
    VaultSettingsDto updateBackupStatus(VaultSettings.BackupStatus status);
} 