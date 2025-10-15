package com.zapp.user_service.dto;

import com.zapp.user_service.enums.UserRole;
import com.zapp.user_service.enums.UserStatus;
import jakarta.validation.constraints.*;

import java.util.Optional;

public record UserPageRequestDto(

        @Min(value = 0, message = "Page number must be >= 0")
        int page,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must be at most 100")
        int size,

        @NotBlank(message = "sortBy is required")
        String sortBy,

        @Pattern(
                regexp = "ASC|DESC",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "sortDir must be either 'ASC' or 'DESC'")
        String sortDir,

        @Size(max = 255, message = "Username filter must be at most 255 characters")
        String username,

        @Email(message = "Email filter must be valid")
        @Size(max = 255, message = "Email filter must be at most 255 characters")
        String email,

        UserStatus status,

        UserRole role

) {
    public UserPageRequestDto {
        // Pagination defaults
        page = (page < 0) ? 0 : page;
        size = (size <= 0) ? 10 : Math.min(size, 100);

        // Sorting defaults
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        sortDir = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir.trim().toUpperCase();

        // Null-safe filter trimming
        username = (username != null && !username.isBlank()) ? username.trim() : null;
        email = (email != null && !email.isBlank()) ? email.trim() : null;
    }
}

