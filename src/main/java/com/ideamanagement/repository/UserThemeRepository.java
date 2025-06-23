package com.ideamanagement.repository;

import com.ideamanagement.entity.UserTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserThemeRepository extends JpaRepository<UserTheme, UUID> {
    Optional<UserTheme> findByUserId(UUID userId);
} 