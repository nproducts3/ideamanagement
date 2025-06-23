package com.ideamanagement.service;

import com.ideamanagement.dto.LikeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LikeService {
    LikeDto createLike(UUID userId, UUID ideaId);
    void deleteLike(UUID userId, UUID ideaId);
    boolean hasLiked(UUID userId, UUID ideaId);
    long getLikeCount(UUID ideaId);
    Page<LikeDto> getLikesByIdea(UUID ideaId, Pageable pageable);
    Page<LikeDto> getLikesByUser(UUID userId, Pageable pageable);
} 