package com.ideamanagement.service.impl;

import com.ideamanagement.dto.UserThemeDto;
import com.ideamanagement.entity.User;
import com.ideamanagement.entity.UserTheme;
import com.ideamanagement.repository.UserRepository;
import com.ideamanagement.repository.UserThemeRepository;
import com.ideamanagement.service.UserThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserThemeServiceImpl implements UserThemeService {
    private final UserThemeRepository userThemeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserThemeDto createUserTheme(UserThemeDto userThemeDto) {
        User user = userRepository.findById(userThemeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserTheme userTheme = new UserTheme();
        userTheme.setUser(user);
        userTheme.setTheme(userThemeDto.getTheme());

        UserTheme savedTheme = userThemeRepository.save(userTheme);
        return convertToDto(savedTheme);
    }

    @Override
    @Transactional(readOnly = true)
    public UserThemeDto getUserTheme(UUID userId) {
        UserTheme userTheme = userThemeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User theme not found"));
        return convertToDto(userTheme);
    }

    @Override
    @Transactional
    public UserThemeDto updateUserTheme(UUID userId, UserTheme.ThemeType theme) {
        UserTheme userTheme = userThemeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User theme not found"));
        userTheme.setTheme(theme);
        UserTheme updatedTheme = userThemeRepository.save(userTheme);
        return convertToDto(updatedTheme);
    }

    @Override
    @Transactional
    public void deleteUserTheme(UUID userId) {
        UserTheme userTheme = userThemeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User theme not found"));
        userThemeRepository.delete(userTheme);
    }

    private UserThemeDto convertToDto(UserTheme userTheme) {
        UserThemeDto dto = new UserThemeDto();
        dto.setId(userTheme.getId());
        dto.setUserId(userTheme.getUser().getId());
        dto.setTheme(userTheme.getTheme());
        dto.setCreatedAt(userTheme.getCreatedAt());
        dto.setUpdatedAt(userTheme.getUpdatedAt());
        return dto;
    }
} 