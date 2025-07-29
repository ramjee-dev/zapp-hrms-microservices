package com.zapp.job_service.dto;

import com.zapp.job_service.enums.JobStatus;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record JobPageRequestDto(
        @Min(0)
        int page,

        @Min(1)
        @Max(100)
        int size,

        @NotBlank
        String sortBy,

        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE, message = "sortDir must be 'ASC' or 'DESC'")
        String sortDir,

        UUID clientId,

        JobStatus status,

        @Size(max = 255, message = "Department name must be at most 255 characters")
        String department,

        @Size(max = 255, message = "Location must be at most 255 characters")
        String location,

        @Size(max = 255, message = "Title must be at most 255 characters")
        String title
) {
    // Compact constructor for null/default safety and trimming input
    public JobPageRequestDto {
        // Enforce defaults
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Optional fields trimming for consistency (null safe)
        department = (department != null && !department.isBlank()) ? department.trim() : null;
        location  = (location  != null && !location.isBlank())  ? location.trim()  : null;
        title     = (title     != null && !title.isBlank())     ? title.trim()     : null;
    }
}
