package com.zapp.candidate_service.dto;

import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.enums.ExperienceLevel;
import com.zapp.candidate_service.validation.AllowedSortBy;
import com.zapp.candidate_service.validation.SortDirection;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CandidatePageRequestDto(

        @Min(value = 0, message = "Page number must be >= 0")
        int page,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must be at most 100")
        int size,

        @NotBlank(message = "sortBy is required")
        @AllowedSortBy(fields = {"createdAt", "firstName", "lastName", "email", "experienceLevel"}, message = "Invalid sortBy field")
        String sortBy,

        @NotBlank(message = "sortDir is required")
        @SortDirection
        String sortDir,

        UUID jobId,
        CandidateStatus status,
        ExperienceLevel experienceLevel,

        @Size(max = 255) String skills,
        @Size(max = 255) String country,
        @Size(max = 255) String firstName,
        @Size(max = 255) String lastName

) {
    public CandidatePageRequestDto {
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);

        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        skills = (skills != null && !skills.isBlank()) ? skills.trim() : null;
        country = (country != null && !country.isBlank()) ? country.trim() : null;
        firstName = (firstName != null && !firstName.isBlank()) ? firstName.trim() : null;
        lastName = (lastName != null && !lastName.isBlank()) ? lastName.trim() : null;
    }
}

