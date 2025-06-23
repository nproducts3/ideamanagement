package com.ideamanagement.service;

import com.ideamanagement.dto.IdeaDto;
import com.ideamanagement.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IdeaService {
    IdeaDto createIdea(IdeaDto ideaDto);
    IdeaDto updateIdea(UUID id, IdeaDto ideaDto);
    IdeaDto getIdeaById(UUID id);
    Page<IdeaDto> getAllIdeas(Pageable pageable);
    Page<IdeaDto> getIdeasByAssignee(String assignee, Pageable pageable);
    Page<IdeaDto> getIdeasByStatus(Idea.Status status, Pageable pageable);
    Page<IdeaDto> getIdeasByTag(String tag, Pageable pageable);
    void deleteIdea(UUID id);
} 