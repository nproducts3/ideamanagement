package com.ideamanagement.service.impl;

import com.ideamanagement.dto.RoleDto;
import com.ideamanagement.entity.Role;
import com.ideamanagement.repository.RoleRepository;
import com.ideamanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new RuntimeException("Role with this name already exists");
        }

        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return convertToDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDto> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public RoleDto updateRole(UUID id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!role.getName().equals(roleDto.getName()) && 
            roleRepository.existsByName(roleDto.getName())) {
            throw new RuntimeException("Role with this name already exists");
        }

        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Role updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }

    private RoleDto convertToDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        return dto;
    }
} 