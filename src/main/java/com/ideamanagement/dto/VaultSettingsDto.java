package com.ideamanagement.dto;

import com.ideamanagement.entity.VaultSettings;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Vault settings data transfer object")
public class VaultSettingsDto {
    @Schema(description = "Unique identifier of the vault settings", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Encryption status (ENABLED, DISABLED)", example = "ENABLED")
    private VaultSettings.EncryptionStatus encryption;

    @Schema(description = "Backup status (ACTIVE, INACTIVE)", example = "ACTIVE")
    private VaultSettings.BackupStatus backup;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
} 