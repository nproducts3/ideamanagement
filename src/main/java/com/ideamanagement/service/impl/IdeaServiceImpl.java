package com.ideamanagement.service.impl;

import com.ideamanagement.dto.IdeaDto;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.repository.IdeaRepository;
import com.ideamanagement.service.IdeaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class IdeaServiceImpl implements IdeaService {
    private final IdeaRepository ideaRepository;

    public IdeaServiceImpl(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    @Override
    @Transactional
    public IdeaDto createIdea(IdeaDto ideaDto) {
        Idea idea = new Idea();
        copyFromDto(idea, ideaDto);
        
        // Set a new UUID for the idea
        idea.setId(UUID.randomUUID());
        
        // Set creation date and time
        idea.setCreatedDate(LocalDate.now());
        idea.setCreatedAt(LocalDateTime.now());
        
        // Set default values
        idea.setUpvotes(0);
        idea.setComments(0);
        if (idea.getStatus() == null) {
            idea.setStatus(Idea.Status.PENDING);
        }
        
        Idea savedIdea = ideaRepository.save(idea);
        return copyToDto(savedIdea);
    }

    @Override
    @Transactional
    public IdeaDto updateIdea(UUID id, IdeaDto ideaDto) {
        Idea idea = ideaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Idea not found with id: " + id));
        
        copyFromDto(idea, ideaDto);
        Idea updatedIdea = ideaRepository.save(idea);
        return copyToDto(updatedIdea);
    }

    @Override
    @Transactional
    public void deleteIdea(UUID id) {
        if (!ideaRepository.existsById(id)) {
            throw new EntityNotFoundException("Idea not found with id: " + id);
        }
        ideaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public IdeaDto getIdeaById(UUID id) {
        Idea idea = ideaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Idea not found with id: " + id));
        return copyToDto(idea);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdeaDto> getAllIdeas(Pageable pageable) {
        return ideaRepository.findAll(pageable)
            .map(this::copyToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdeaDto> getIdeasByAssignee(String assignee, Pageable pageable) {
        return ideaRepository.findByAssignedTo(assignee, pageable)
            .map(this::copyToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdeaDto> getIdeasByStatus(Idea.Status status, Pageable pageable) {
        return ideaRepository.findByStatus(status, pageable)
            .map(this::copyToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdeaDto> getIdeasByTag(String tag, Pageable pageable) {
        return ideaRepository.findByTagsContaining(tag, pageable)
            .map(this::copyToDto);
    }

    private void copyFromDto(Idea idea, IdeaDto dto) {
        idea.setTitle(dto.getTitle());
        idea.setDescription(dto.getDescription());
        idea.setPriority(dto.getPriority());
        idea.setStatus(dto.getStatus());
        idea.setAssignedTo(dto.getAssignedTo());
        idea.setDueDate(dto.getDueDate());
        if (dto.getTags() != null) {
            idea.setTags(dto.getTags());
        }
    }

    private IdeaDto copyToDto(Idea idea) {
        IdeaDto dto = new IdeaDto();
        dto.setId(idea.getId());
        dto.setTitle(idea.getTitle());
        dto.setDescription(idea.getDescription());
        dto.setPriority(idea.getPriority());
        dto.setStatus(idea.getStatus());
        dto.setAssignedTo(idea.getAssignedTo());
        dto.setUpvotes(idea.getUpvotes());
        dto.setComments(idea.getComments());
        dto.setDueDate(idea.getDueDate());
        dto.setCreatedDate(idea.getCreatedDate());
        dto.setCreatedAt(idea.getCreatedAt());
        dto.setTags(idea.getTags());
        return dto;
    }
} 