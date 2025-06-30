package com.ideamanagement.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TagDto {
    private String id;
    private String name;
    private UUID employeeId;
} 