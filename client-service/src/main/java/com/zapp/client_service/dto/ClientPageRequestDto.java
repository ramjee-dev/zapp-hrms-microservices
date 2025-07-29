package com.zapp.client_service.dto;

import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.enums.ClientType;
import jakarta.validation.constraints.*;

public record ClientPageRequestDto(
        @Min(0)
        int page,

        @Min(1)
        @Max(100)
        int size,

        @NotBlank
        String sortBy,

        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "sortDir must be 'ASC' or 'DESC'")
        String sortDir,

        // Filters
        ClientStatus status,
        ClientType clientType,

        @Size(max = 255, message = "Industry must be at most 255 characters")
        String industry,

        @Size(max = 255, message = "Country must be at most 255 characters")
        String country,

        @Size(max = 255, message = "Company name must be at most 255 characters")
        String companyName
) {
    public ClientPageRequestDto {
        // Enforce defaults
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Optional fields trimming for consistency (null safe)
        industry = (industry != null && !industry.isBlank()) ? industry.trim() : null;
        country  = (country  != null && !country.isBlank())  ? country.trim()  : null;
        companyName = (companyName != null && !companyName.isBlank()) ? companyName.trim() : null;
    }
}
