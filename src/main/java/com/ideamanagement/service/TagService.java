package com.ideamanagement.service;

import com.ideamanagement.dto.TagDto;
import com.ideamanagement.entity.Tag;
import com.ideamanagement.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDto createTag(TagDto tagDto) {
        if (tagRepository.existsByName(tagDto.getName())) {
            throw new IllegalArgumentException("Tag name already exists");
        }

        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDto, tag);
        tag = tagRepository.save(tag);
        
        TagDto savedTagDto = new TagDto();
        BeanUtils.copyProperties(tag, savedTagDto);
        return savedTagDto;
    }

    public TagDto updateTag(String id, TagDto tagDto) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        if (!tag.getName().equals(tagDto.getName()) && 
            tagRepository.existsByName(tagDto.getName())) {
            throw new IllegalArgumentException("Tag name already exists");
        }

        BeanUtils.copyProperties(tagDto, tag, "id");
        tag = tagRepository.save(tag);
        
        TagDto updatedTagDto = new TagDto();
        BeanUtils.copyProperties(tag, updatedTagDto);
        return updatedTagDto;
    }

    public TagDto getTagById(String id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        
        TagDto tagDto = new TagDto();
        BeanUtils.copyProperties(tag, tagDto);
        return tagDto;
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
            .map(tag -> {
                TagDto tagDto = new TagDto();
                BeanUtils.copyProperties(tag, tagDto);
                return tagDto;
            })
            .collect(Collectors.toList());
    }

    public void deleteTag(String id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found");
        }
        tagRepository.deleteById(id);
    }
} 