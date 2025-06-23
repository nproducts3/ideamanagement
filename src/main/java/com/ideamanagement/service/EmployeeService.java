package com.ideamanagement.service;

import com.ideamanagement.dto.EmployeeDto;
import com.ideamanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(UUID id, EmployeeDto employeeDto);
    void deleteEmployee(UUID id);
    EmployeeDto getEmployeeById(UUID id);
    EmployeeDto getEmployeeByEmail(String email);
    Page<EmployeeDto> getAllEmployees(Pageable pageable);
    Page<EmployeeDto> getEmployeesByStatus(Employee.Status status, Pageable pageable);
    Page<EmployeeDto> getEmployeesByDepartment(String department, Pageable pageable);
    boolean existsByEmail(String email);
} 