package com.ideamanagement.service;

import com.ideamanagement.dto.IntegrationSettingsDto;
import com.ideamanagement.entity.IntegrationSettings;

import java.util.List;
import java.util.UUID;

public interface IntegrationSettingsService {
    IntegrationSettingsDto createIntegrationSettings(IntegrationSettingsDto settingsDto);
    IntegrationSettingsDto getIntegrationSettings(UUID id);
    List<IntegrationSettingsDto> getIntegrationSettingsByUser(UUID userId);
    IntegrationSettingsDto getIntegrationSettingsByUserAndType(UUID userId, IntegrationSettings.IntegrationType type);
    IntegrationSettingsDto updateIntegrationSettings(UUID id, IntegrationSettingsDto settingsDto);
    IntegrationSettingsDto updateConnectionStatus(UUID id, boolean connected);
    void deleteIntegrationSettings(UUID id);
} 