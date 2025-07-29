package com.zapp.job_service.dto;

import com.zapp.job_service.enums.EmploymentType;
import com.zapp.job_service.enums.JobPriority;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public record JobResponseDto(

        UUID id,
        String title,
        String description,
        UUID clientId,
        String location,
        String department,
        JobStatus status,
        JobPriority priority,
        String requiredSkills,
        String experienceRequired,
        String salaryRange,
        EmploymentType employmentType,
        Integer positionsAvailable,
        String createdAt,
        String updatedAt,
        String createdBy,
        String updatedBy
) {
}
