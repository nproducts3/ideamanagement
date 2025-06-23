package com.ideamanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "vault_settings")
public class VaultSettings {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "encryption", nullable = false)
    private EncryptionStatus encryption = EncryptionStatus.ENABLED;

    @Enumerated(EnumType.STRING)
    @Column(name = "backup", nullable = false)
    private BackupStatus backup = BackupStatus.ACTIVE;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EncryptionStatus {
        ENABLED, DISABLED
    }

    public enum BackupStatus {
        ACTIVE, INACTIVE
    }
} 