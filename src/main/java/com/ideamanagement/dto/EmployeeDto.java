package com.ideamanagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class EmployeeDto {
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @Size(max = 50)
    private String phone;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String position;

    @NotNull(message = "Status is required")
    private String status;

    private LocalDate hireDate;

    @Digits(integer = 13, fraction = 2)
    private BigDecimal salary;

    @Size(max = 255)
    private String address;

    @Size(max = 255)
    private String avatar;

    @Size(max = 100)
    private String manager;

    private Set<String> skills;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 