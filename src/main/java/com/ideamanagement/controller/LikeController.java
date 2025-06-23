package com.ideamanagement.controller;

import com.ideamanagement.dto.LikeDto;
import com.ideamanagement.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/ideas/{ideaId}/users/{userId}")
    public ResponseEntity<LikeDto> createLike(@PathVariable UUID ideaId, @PathVariable UUID userId) {
        return ResponseEntity.ok(likeService.createLike(userId, ideaId));
    }

    @DeleteMapping("/ideas/{ideaId}/users/{userId}")
    public ResponseEntity<Void> deleteLike(@PathVariable UUID ideaId, @PathVariable UUID userId) {
        likeService.deleteLike(userId, ideaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ideas/{ideaId}/users/{userId}/check")
    public ResponseEntity<Boolean> hasLiked(@PathVariable UUID ideaId, @PathVariable UUID userId) {
        return ResponseEntity.ok(likeService.hasLiked(userId, ideaId));
    }

    @GetMapping("/ideas/{ideaId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable UUID ideaId) {
        return ResponseEntity.ok(likeService.getLikeCount(ideaId));
    }

    @GetMapping("/ideas/{ideaId}")
    public ResponseEntity<Page<LikeDto>> getLikesByIdea(@PathVariable UUID ideaId, Pageable pageable) {
        return ResponseEntity.ok(likeService.getLikesByIdea(ideaId, pageable));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<LikeDto>> getLikesByUser(@PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(likeService.getLikesByUser(userId, pageable));
    }
} 