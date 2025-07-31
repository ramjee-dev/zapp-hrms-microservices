package com.zapp.candidate_service.dto;

import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.enums.ExperienceLevel;

import java.math.BigDecimal;
import java.util.UUID;

public record CandidateResponseDto(

        UUID id,

        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String postalCode,
        String country,

        UUID jobId,
        CandidateStatus status,
        ExperienceLevel experienceLevel,
        BigDecimal yearsOfExperience,
        String skills,
        String currentPosition,
        String currentCompany,
        BigDecimal currentSalary,
        BigDecimal expectedSalary,
        String noticePeriod,
        String resumeUrl,
        String linkedinProfile,
        String portfolioUrl,
        String notes,
        UUID assignedTo,

        // Audit fields as ISO-8601 formatted Strings
        String createdAt,
        String updatedAt,
        String createdBy,
        String updatedBy
) {}
