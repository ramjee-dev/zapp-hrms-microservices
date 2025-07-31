package com.zapp.job_service.dto;

import com.zapp.job_service.enums.EmploymentType;
import com.zapp.job_service.enums.JobPriority;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateJobRequestDto(

        @NotBlank(message = "Job title is required")
        @Size(min = 3, max = 200, message = "Job title must be between 3 and 200 characters")
        String title,

        @NotBlank(message = "Job description is required")
        @Size(min = 50, max = 5000, message = "Job description must be between 50 and 5000 characters")
        String description,

        @NotNull(message = "Client ID is required")
        UUID clientId,

        @NotBlank(message = "Location is required")
        @Size(max = 100, message = "Location must not exceed 100 characters")
        String location,

        @NotBlank(message = "Department is required")
        @Size(max = 100, message = "Department must not exceed 100 characters")
        String department,

        @NotNull(message = "Priority is required")
        JobPriority priority, // proper validation for job priority

        @Size(max = 1000, message = "Required skills must not exceed 1000 characters")
        String requiredSkills,

        @Size(max = 200, message = "Experience required must not exceed 200 characters")
        String experienceRequired,

        @Size(max = 100, message = "Salary range must not exceed 100 characters")
        String salaryRange,

        @NotNull(message = "Employment type is required")
        EmploymentType employmentType, // proper validation for employmentType

        @Min(value = 1, message = "Positions available must be at least 1")
        @Max(value = 100, message = "Positions available must not exceed 100")
        Integer positionsAvailable
) {}
