package com.ideamanagement.repository;

import com.ideamanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);
    Page<Employee> findByStatus(Employee.Status status, Pageable pageable);
    Page<Employee> findByDepartment(String department, Pageable pageable);
    boolean existsByEmail(String email);
} 