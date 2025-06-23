package com.ideamanagement.service;

import com.ideamanagement.dto.UserThemeDto;
import com.ideamanagement.entity.UserTheme;

import java.util.UUID;

public interface UserThemeService {
    UserThemeDto createUserTheme(UserThemeDto userThemeDto);
    UserThemeDto getUserTheme(UUID userId);
    UserThemeDto updateUserTheme(UUID userId, UserTheme.ThemeType theme);
    void deleteUserTheme(UUID userId);
} 