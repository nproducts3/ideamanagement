package com.ideamanagement.service.impl;

import com.ideamanagement.dto.IntegrationSettingsDto;
import com.ideamanagement.entity.IntegrationSettings;
import com.ideamanagement.entity.User;
import com.ideamanagement.repository.IntegrationSettingsRepository;
import com.ideamanagement.repository.UserRepository;
import com.ideamanagement.service.IntegrationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntegrationSettingsServiceImpl implements IntegrationSettingsService {
    private final IntegrationSettingsRepository integrationSettingsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public IntegrationSettingsDto createIntegrationSettings(IntegrationSettingsDto settingsDto) {
        User user = userRepository.findById(settingsDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (integrationSettingsRepository.existsByUserIdAndType(user.getId(), settingsDto.getType())) {
            throw new RuntimeException("Integration settings for this type already exist");
        }

        IntegrationSettings settings = new IntegrationSettings();
        settings.setUser(user);
        settings.setType(settingsDto.getType());
        settings.setConnected(settingsDto.isConnected());
        settings.setConfig(settingsDto.getConfig());

        IntegrationSettings savedSettings = integrationSettingsRepository.save(settings);
        return convertToDto(savedSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationSettingsDto getIntegrationSettings(UUID id) {
        IntegrationSettings settings = integrationSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration settings not found"));
        return convertToDto(settings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntegrationSettingsDto> getIntegrationSettingsByUser(UUID userId) {
        return integrationSettingsRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationSettingsDto getIntegrationSettingsByUserAndType(UUID userId, IntegrationSettings.IntegrationType type) {
        IntegrationSettings settings = integrationSettingsRepository.findByUserIdAndType(userId, type)
                .orElseThrow(() -> new RuntimeException("Integration settings not found"));
        return convertToDto(settings);
    }

    @Override
    @Transactional
    public IntegrationSettingsDto updateIntegrationSettings(UUID id, IntegrationSettingsDto settingsDto) {
        IntegrationSettings settings = integrationSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration settings not found"));

        settings.setType(settingsDto.getType());
        settings.setConnected(settingsDto.isConnected());
        settings.setConfig(settingsDto.getConfig());

        IntegrationSettings updatedSettings = integrationSettingsRepository.save(settings);
        return convertToDto(updatedSettings);
    }

    @Override
    @Transactional
    public IntegrationSettingsDto updateConnectionStatus(UUID id, boolean connected) {
        IntegrationSettings settings = integrationSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration settings not found"));
        settings.setConnected(connected);
        IntegrationSettings updatedSettings = integrationSettingsRepository.save(settings);
        return convertToDto(updatedSettings);
    }

    @Override
    @Transactional
    public void deleteIntegrationSettings(UUID id) {
        IntegrationSettings settings = integrationSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration settings not found"));
        integrationSettingsRepository.delete(settings);
    }

    private IntegrationSettingsDto convertToDto(IntegrationSettings settings) {
        IntegrationSettingsDto dto = new IntegrationSettingsDto();
        dto.setId(settings.getId());
        dto.setUserId(settings.getUser().getId());
        dto.setType(settings.getType());
        dto.setConnected(settings.isConnected());
        dto.setConfig(settings.getConfig());
        dto.setCreatedAt(settings.getCreatedAt());
        dto.setUpdatedAt(settings.getUpdatedAt());
        return dto;
    }
} 