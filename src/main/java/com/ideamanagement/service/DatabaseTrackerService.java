package com.ideamanagement.service;

import com.ideamanagement.dto.DatabaseTrackerDto;
import com.ideamanagement.entity.DatabaseTracker;
import com.ideamanagement.entity.DatabaseTracker.Status;
import com.ideamanagement.repository.DatabaseTrackerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DatabaseTrackerService {
    private final DatabaseTrackerRepository databaseTrackerRepository;

    public DatabaseTrackerService(DatabaseTrackerRepository databaseTrackerRepository) {
        this.databaseTrackerRepository = databaseTrackerRepository;
    }

    public DatabaseTrackerDto createDatabaseTracker(DatabaseTrackerDto dto) {
        if (databaseTrackerRepository.existsByName(dto.getName())) {
            throw new IllegalStateException("Database tracker with name " + dto.getName() + " already exists");
        }

        DatabaseTracker entity = new DatabaseTracker();
        copyToEntity(dto, entity);
        entity.setLastModified(LocalDate.now());
        entity = databaseTrackerRepository.save(entity);
        return copyToDto(entity);
    }

    public DatabaseTrackerDto updateDatabaseTracker(Integer id, java.util.UUID employeeId, DatabaseTrackerDto dto) {
        DatabaseTracker entity = databaseTrackerRepository.findByIdAndEmployeeId(id, employeeId);
        if (entity == null) {
            throw new EntityNotFoundException("Database tracker not found with id: " + id + " for employee: " + employeeId);
        }
        if (!entity.getName().equals(dto.getName()) && databaseTrackerRepository.existsByName(dto.getName())) {
            throw new IllegalStateException("Database tracker with name " + dto.getName() + " already exists");
        }
        copyToEntity(dto, entity);
        entity.setLastModified(java.time.LocalDate.now());
        entity = databaseTrackerRepository.save(entity);
        return copyToDto(entity);
    }

    public DatabaseTrackerDto getDatabaseTrackerById(Integer id, java.util.UUID employeeId) {
        DatabaseTracker entity = databaseTrackerRepository.findByIdAndEmployeeId(id, employeeId);
        if (entity == null) {
            throw new EntityNotFoundException("Database tracker not found with id: " + id + " for employee: " + employeeId);
        }
        return copyToDto(entity);
    }

    public DatabaseTrackerDto getDatabaseTrackerByName(String name) {
        DatabaseTracker entity = databaseTrackerRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Database tracker not found with name: " + name));
        return copyToDto(entity);
    }

    public org.springframework.data.domain.Page<DatabaseTrackerDto> getAllDatabaseTrackers(java.util.UUID employeeId, org.springframework.data.domain.Pageable pageable) {
        return databaseTrackerRepository.findByEmployeeId(employeeId, pageable).map(this::copyToDto);
    }

    public Page<DatabaseTrackerDto> getDatabaseTrackersByStatus(Status status, Pageable pageable) {
        return databaseTrackerRepository.findByStatus(status, pageable)
            .map(this::copyToDto);
    }

    public Page<DatabaseTrackerDto> getDatabaseTrackersByVersion(String version, Pageable pageable) {
        return databaseTrackerRepository.findByVersion(version, pageable)
            .map(this::copyToDto);
    }

    public void deleteDatabaseTracker(Integer id, java.util.UUID employeeId) {
        DatabaseTracker entity = databaseTrackerRepository.findByIdAndEmployeeId(id, employeeId);
        if (entity == null) {
            throw new EntityNotFoundException("Database tracker not found with id: " + id + " for employee: " + employeeId);
        }
        databaseTrackerRepository.deleteByIdAndEmployeeId(id, employeeId);
    }

    public DatabaseTrackerDto updateStatus(Integer id, Status status) {
        DatabaseTracker entity = databaseTrackerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Database tracker not found with id: " + id));
        
        entity.setStatus(status);
        entity.setLastModified(LocalDate.now());
        entity = databaseTrackerRepository.save(entity);
        return copyToDto(entity);
    }

    private void copyToEntity(DatabaseTrackerDto dto, DatabaseTracker entity) {
        BeanUtils.copyProperties(dto, entity, "id", "lastModified", "employee");
        if (dto.getMigrationsJson() == null) {
            entity.setMigrationsJson("[]");
        }
        if (dto.getTablesCount() == null) {
            entity.setTablesCount(0);
        }
        if (dto.getMigrationsCount() == null) {
            entity.setMigrationsCount(0);
        }
        if (dto.getEmployeeId() != null) {
            com.ideamanagement.entity.Employee emp = new com.ideamanagement.entity.Employee();
            emp.setId(dto.getEmployeeId());
            entity.setEmployee(emp);
        }
    }

    private DatabaseTrackerDto copyToDto(DatabaseTracker entity) {
        DatabaseTrackerDto dto = new DatabaseTrackerDto();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
        }
        return dto;
    }
} 