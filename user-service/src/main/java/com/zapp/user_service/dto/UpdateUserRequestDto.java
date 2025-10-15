package com.zapp.user_service.dto;

import com.zapp.user_service.enums.UserRole;
import com.zapp.user_service.enums.UserStatus;
import jakarta.validation.constraints.*;

public record UpdateUserRequestDto(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phoneNumber,

        @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
        String avatarUrl,

        @Size(max = 50, message = "Time zone must not exceed 50 characters")
        String timeZone,

        @Size(max = 10, message = "Locale must not exceed 10 characters")
        String locale,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Role is required")
        UserRole role,

        @NotNull(message = "Status is required")
        UserStatus status
) {}
