package com.zapp.client_service.dto;

import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.enums.ClientType;
import com.zapp.client_service.validations.ValidSortBy;
import com.zapp.client_service.validations.ValidSortDir;
import jakarta.validation.constraints.*;

import java.util.Optional;

public record ClientPageRequestDto(

        @Min(value = 0, message = "Page number must be >= 0")
        int page,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must be at most 100")
        int size,

        @NotBlank(message = "sortBy is required")
        @ValidSortBy(allowed = {"companyName", "contactPerson", "email", "country", "industry",
                "createdAt", "employeeCount"}, message = "Invalid sortBy field")
        String sortBy,

        @ValidSortDir(message = "Invalid sortDir field, sortDir must be either 'ASC' or 'DESC'")
        String sortDir,

        ClientStatus status,

        ClientType clientType,

        @Size(max = 255, message = "Industry filter must be at most 255 characters")
        String industry,

        @Size(max = 255, message = "Country filter must be at most 255 characters")
        String country,

        @Size(max = 255, message = "Company name filter must be at most 255 characters")
        String companyName

) {
    public ClientPageRequestDto {
        // Pagination and sorting defaults
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);

        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Null-safe trimming
        industry = (industry != null && !industry.isBlank()) ? industry.trim() : null;
        country = (country != null && !country.isBlank()) ? country.trim() : null;
        companyName = (companyName != null && !companyName.isBlank()) ? companyName.trim() : null;
    }
}

