package com.zapp.job_service.dto;

import com.zapp.job_service.enums.JobStatus;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record JobPageRequestDto(

        @Min(value = 0, message = "Page number must be >= 0")
        int page,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must be at most 100")
        int size,

        @NotBlank(message = "sortBy is required")
        String sortBy,

        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "sortDir must be either 'ASC' or 'DESC'")
        String sortDir,

        UUID clientId,

        JobStatus status,

        @Size(max = 255, message = "Department filter must be at most 255 characters")
        String department,

        @Size(max = 255, message = "Location filter must be at most 255 characters")
        String location,

        @Size(max = 255, message = "Title filter must be at most 255 characters")
        String title

) {
    public JobPageRequestDto {
        // Pagination and sorting defaults
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);

        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Null-safe trimming for filters
        department = (department != null && !department.isBlank()) ? department.trim() : null;
        location = (location != null && !location.isBlank()) ? location.trim() : null;
        title = (title != null && !title.isBlank()) ? title.trim() : null;
    }
}

