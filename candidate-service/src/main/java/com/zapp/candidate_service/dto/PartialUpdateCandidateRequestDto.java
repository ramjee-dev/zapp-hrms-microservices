package com.zapp.candidate_service.dto;

import com.zapp.candidate_service.enums.ExperienceLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record PartialUpdateCandidateRequestDto(

        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @Email(message = "Email must be valid")
        String email,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phone,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Size(max = 100, message = "State must not exceed 100 characters")
        String state,

        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        UUID jobId,

        ExperienceLevel experienceLevel,

        @DecimalMin(value = "0.0", inclusive = true, message = "Years of experience must be non-negative")
        BigDecimal yearsOfExperience,

        @Size(max = 1000, message = "Skills must not exceed 1000 characters")
        String skills,

        @Size(max = 200, message = "Current position must not exceed 200 characters")
        String currentPosition,

        @Size(max = 200, message = "Current company must not exceed 200 characters")
        String currentCompany,

        @DecimalMin(value = "0.0", inclusive = true, message = "Current salary must be non-negative")
        BigDecimal currentSalary,

        @DecimalMin(value = "0.0", inclusive = true, message = "Expected salary must be non-negative")
        BigDecimal expectedSalary,

        @Size(max = 200, message = "Notice period must not exceed 200 characters")
        String noticePeriod,

        @Size(max = 500, message = "Resume URL must not exceed 500 characters")
        String resumeUrl,

        @Size(max = 200, message = "LinkedIn profile must not exceed 200 characters")
        String linkedinProfile,

        @Size(max = 200, message = "Portfolio URL must not exceed 200 characters")
        String portfolioUrl,

        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        String notes,

        UUID assignedTo
) {}
