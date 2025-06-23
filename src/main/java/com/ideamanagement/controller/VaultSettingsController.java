package com.ideamanagement.controller;

import com.ideamanagement.dto.VaultSettingsDto;
import com.ideamanagement.entity.VaultSettings;
import com.ideamanagement.service.VaultSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vault-settings")
@RequiredArgsConstructor
@Tag(name = "Vault Settings", description = "APIs for managing vault settings")
public class VaultSettingsController {
    private final VaultSettingsService vaultSettingsService;

    @GetMapping
    @Operation(summary = "Get vault settings", description = "Retrieves the current vault settings")
    public ResponseEntity<VaultSettingsDto> getVaultSettings() {
        return ResponseEntity.ok(vaultSettingsService.getVaultSettings());
    }

    @PutMapping
    @Operation(summary = "Update vault settings", description = "Updates the vault settings with new values")
    public ResponseEntity<VaultSettingsDto> updateVaultSettings(
            @Parameter(description = "Updated vault settings") @RequestBody VaultSettingsDto vaultSettingsDto) {
        return ResponseEntity.ok(vaultSettingsService.updateVaultSettings(vaultSettingsDto));
    }

    @PatchMapping("/encryption")
    @Operation(summary = "Update encryption status", description = "Updates the encryption status of the vault")
    public ResponseEntity<VaultSettingsDto> updateEncryptionStatus(
            @Parameter(description = "New encryption status") @RequestBody VaultSettings.EncryptionStatus status) {
        return ResponseEntity.ok(vaultSettingsService.updateEncryptionStatus(status));
    }

    @PatchMapping("/backup")
    @Operation(summary = "Update backup status", description = "Updates the backup status of the vault")
    public ResponseEntity<VaultSettingsDto> updateBackupStatus(
            @Parameter(description = "New backup status") @RequestBody VaultSettings.BackupStatus status) {
        return ResponseEntity.ok(vaultSettingsService.updateBackupStatus(status));
    }
} 