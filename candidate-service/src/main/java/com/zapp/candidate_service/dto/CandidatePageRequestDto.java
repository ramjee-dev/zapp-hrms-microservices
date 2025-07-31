package com.zapp.candidate_service.dto;

import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.enums.ExperienceLevel;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CandidatePageRequestDto(

        @Min(value = 0, message = "Page number must be >= 0")
        int page,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must be at most 100")
        int size,

        @NotBlank(message = "sortBy is required")
        String sortBy,

        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "sortDir must be 'ASC' or 'DESC'")
        String sortDir,

        UUID jobId,

        CandidateStatus status,

        ExperienceLevel experienceLevel,

        @Size(max = 255, message = "Skills filter must be at most 255 characters")
        String skills,

        @Size(max = 255, message = "Country filter must be at most 255 characters")
        String country,

        @Size(max = 255, message = "First name filter must be at most 255 characters")
        String firstName,

        @Size(max = 255, message = "Last name filter must be at most 255 characters")
        String lastName
) {
    // Compact constructor for null/default safety and trimming input
    public CandidatePageRequestDto {
        // Validate and set default values for pagination and sorting
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);

        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();

        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Null-safe trimming for filters
        skills = (skills != null && !skills.isBlank()) ? skills.trim() : null;
        country = (country != null && !country.isBlank()) ? country.trim() : null;
        firstName = (firstName != null && !firstName.isBlank()) ? firstName.trim() : null;
        lastName = (lastName != null && !lastName.isBlank()) ? lastName.trim() : null;
    }
}
