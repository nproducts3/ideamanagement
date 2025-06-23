package com.ideamanagement.service.impl;

import com.ideamanagement.dto.EmployeeDto;
import com.ideamanagement.entity.Employee;
import com.ideamanagement.exception.EntityNotFoundException;
import com.ideamanagement.repository.EmployeeRepository;
import com.ideamanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(UUID id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        modelMapper.map(employeeDto, employee);
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
            .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getEmployeesByStatus(Employee.Status status, Pageable pageable) {
        return employeeRepository.findByStatus(status, pageable)
            .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getEmployeesByDepartment(String department, Pageable pageable) {
        return employeeRepository.findByDepartment(department, pageable)
            .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
} 