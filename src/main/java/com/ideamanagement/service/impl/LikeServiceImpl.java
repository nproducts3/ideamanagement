package com.ideamanagement.service.impl;

import com.ideamanagement.dto.LikeDto;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.entity.Like;
import com.ideamanagement.entity.User;
import com.ideamanagement.repository.IdeaRepository;
import com.ideamanagement.repository.LikeRepository;
import com.ideamanagement.repository.UserRepository;
import com.ideamanagement.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    @Override
    @Transactional
    public LikeDto createLike(UUID userId, UUID ideaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));

        if (likeRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new RuntimeException("User has already liked this idea");
        }

        Like like = new Like();
        like.setUser(user);
        like.setIdea(idea);

        Like savedLike = likeRepository.save(like);
        
        // Update idea upvotes count
        idea.setUpvotes((int) likeRepository.countByIdeaId(ideaId));
        ideaRepository.save(idea);

        return convertToDto(savedLike);
    }

    @Override
    @Transactional
    public void deleteLike(UUID userId, UUID ideaId) {
        Like like = likeRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        
        likeRepository.delete(like);
        
        // Update idea upvotes count
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));
        idea.setUpvotes((int) likeRepository.countByIdeaId(ideaId));
        ideaRepository.save(idea);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasLiked(UUID userId, UUID ideaId) {
        return likeRepository.existsByUserIdAndIdeaId(userId, ideaId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(UUID ideaId) {
        return likeRepository.countByIdeaId(ideaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeDto> getLikesByIdea(UUID ideaId, Pageable pageable) {
        return likeRepository.findAllByIdeaId(ideaId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeDto> getLikesByUser(UUID userId, Pageable pageable) {
        return likeRepository.findAllByUserId(userId, pageable)
                .map(this::convertToDto);
    }

    private LikeDto convertToDto(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setUserId(like.getUser().getId());
        dto.setIdeaId(like.getIdea().getId());
        dto.setCreatedAt(like.getCreatedAt());
        return dto;
    }
} 