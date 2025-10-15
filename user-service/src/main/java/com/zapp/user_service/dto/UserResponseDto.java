package com.zapp.user_service.dto;

import com.zapp.user_service.enums.UserRole;
import com.zapp.user_service.enums.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String keycloakUserId,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String avatarUrl,
        String timeZone,
        String locale,
        UserRole role,
        UserStatus status,
        boolean emailVerified,
        boolean phoneVerified,
        Instant lastPasswordResetAt,
        int failedLoginAttempts,
        Instant accountLockedUntil,
        String description,

        // Audit fields as ISO-8601 formatted Strings
        String createdAt,
        String createdBy,
        String updatedAt,
        String updatedBy
) {
}
