package com.ideamanagement.service.impl;

import com.ideamanagement.dto.IdeaDto;
import com.ideamanagement.entity.Idea;
import com.ideamanagement.repository.IdeaRepository;
import com.ideamanagement.service.IdeaService;
import com.ideamanagement.repository.EmployeeRepository;
import com.ideamanagement.entity.Employee;
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
    private final EmployeeRepository employeeRepository;

    public IdeaServiceImpl(IdeaRepository ideaRepository, EmployeeRepository employeeRepository) {
        this.ideaRepository = ideaRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public IdeaDto createIdea(IdeaDto ideaDto) {
        Idea idea = new Idea();
        copyFromDto(idea, ideaDto);
        if (ideaDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(ideaDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + ideaDto.getEmployeeId()));
            idea.setEmployee(employee);
        } else {
            idea.setEmployee(null);
        }
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
    public IdeaDto updateIdea(UUID id, UUID employeeId, IdeaDto ideaDto) {
        Idea idea = ideaRepository.findByIdAndEmployeeId(id, employeeId);
        if (idea == null) {
            throw new EntityNotFoundException("Idea not found with id: " + id + " for employee: " + employeeId);
        }
        copyFromDto(idea, ideaDto);
        if (ideaDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(ideaDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + ideaDto.getEmployeeId()));
            idea.setEmployee(employee);
        } else {
            idea.setEmployee(null);
        }
        Idea updatedIdea = ideaRepository.save(idea);
        return copyToDto(updatedIdea);
    }

    @Override
    @Transactional
    public void deleteIdea(UUID id, UUID employeeId) {
        Idea idea = ideaRepository.findByIdAndEmployeeId(id, employeeId);
        if (idea == null) {
            throw new EntityNotFoundException("Idea not found with id: " + id + " for employee: " + employeeId);
        }
        ideaRepository.deleteByIdAndEmployeeId(id, employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public IdeaDto getIdeaById(UUID id, UUID employeeId) {
        Idea idea = ideaRepository.findByIdAndEmployeeId(id, employeeId);
        if (idea == null) {
            throw new EntityNotFoundException("Idea not found with id: " + id + " for employee: " + employeeId);
        }
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
    public Page<IdeaDto> getAllIdeas(UUID employeeId, Pageable pageable) {
        return ideaRepository.findByEmployeeId(employeeId, pageable).map(this::copyToDto);
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

    @Override
    @Transactional
    public IdeaDto patchIdea(UUID id, UUID employeeId, IdeaDto ideaDto) {
        Idea idea = ideaRepository.findByIdAndEmployeeId(id, employeeId);
        if (idea == null) {
            throw new EntityNotFoundException("Idea not found with id: " + id + " for employee: " + employeeId);
        }
        // Only update non-null fields
        if (ideaDto.getTitle() != null) idea.setTitle(ideaDto.getTitle());
        if (ideaDto.getDescription() != null) idea.setDescription(ideaDto.getDescription());
        if (ideaDto.getPriority() != null) idea.setPriority(ideaDto.getPriority());
        if (ideaDto.getStatus() != null) idea.setStatus(ideaDto.getStatus());
        if (ideaDto.getAssignedTo() != null) idea.setAssignedTo(ideaDto.getAssignedTo());
        if (ideaDto.getDueDate() != null) idea.setDueDate(ideaDto.getDueDate());
        if (ideaDto.getTags() != null) {
            idea.getTags().clear();
            idea.getTags().addAll(new java.util.HashSet<>(ideaDto.getTags()));
        }
        if (ideaDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(ideaDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + ideaDto.getEmployeeId()));
            idea.setEmployee(employee);
        }
        Idea updatedIdea = ideaRepository.save(idea);
        return copyToDto(updatedIdea);
    }

    private void copyFromDto(Idea idea, IdeaDto dto) {
        idea.setTitle(dto.getTitle());
        idea.setDescription(dto.getDescription());
        idea.setPriority(dto.getPriority());
        idea.setStatus(dto.getStatus());
        idea.setAssignedTo(dto.getAssignedTo());
        idea.setDueDate(dto.getDueDate());
        if (dto.getTags() != null) {
            idea.getTags().clear();
            idea.getTags().addAll(new java.util.HashSet<>(dto.getTags()));
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
        if (idea.getEmployee() != null) {
            dto.setEmployeeId(idea.getEmployee().getId());
        }
        return dto;
    }
} 