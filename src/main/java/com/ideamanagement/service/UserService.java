package com.ideamanagement.service;

import com.ideamanagement.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(UUID id);
    UserDto getUserByUsername(String username);
    UserDto getUserByEmail(String email);
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto updateUser(UUID id, UserDto userDto);
    void deleteUser(UUID id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 