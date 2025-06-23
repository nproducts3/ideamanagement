package com.ideamanagement.repository;

import com.ideamanagement.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserIdAndIdeaId(UUID userId, UUID ideaId);
    boolean existsByUserIdAndIdeaId(UUID userId, UUID ideaId);
    long countByIdeaId(UUID ideaId);
    Page<Like> findAllByIdeaId(UUID ideaId, Pageable pageable);
    Page<Like> findAllByUserId(UUID userId, Pageable pageable);
} 