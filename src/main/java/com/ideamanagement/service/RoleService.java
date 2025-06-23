package com.ideamanagement.service;

import com.ideamanagement.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRole(UUID id);
    Page<RoleDto> getAllRoles(Pageable pageable);
    RoleDto updateRole(UUID id, RoleDto roleDto);
    void deleteRole(UUID id);
} 