package com.zapp.user_service.dto;

import com.zapp.user_service.enums.UserRole;
import com.zapp.user_service.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record PartialUpdateUserRequestDto(

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

        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        UserRole role,

        UserStatus status
) {}
